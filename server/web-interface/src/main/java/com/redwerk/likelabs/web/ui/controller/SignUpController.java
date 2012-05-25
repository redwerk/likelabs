package com.redwerk.likelabs.web.ui.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.MailSendException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.redwerk.likelabs.application.RegistrationService;
import com.redwerk.likelabs.application.UserService;
import com.redwerk.likelabs.application.impl.registration.exception.DuplicatedUserException;
import com.redwerk.likelabs.application.impl.registration.exception.IncorrectPasswordException;
import com.redwerk.likelabs.application.messaging.MessageTemplateService;
import com.redwerk.likelabs.application.messaging.exception.EmailMessagingException;
import com.redwerk.likelabs.application.messaging.exception.SmsMessagingException;
import com.redwerk.likelabs.domain.model.SocialNetworkType;
import com.redwerk.likelabs.domain.model.user.User;
import com.redwerk.likelabs.domain.model.user.UserSocialAccount;
import com.redwerk.likelabs.web.ui.validator.EmailValidator;
import com.redwerk.likelabs.web.ui.validator.PhoneValidator;
import com.redwerk.likelabs.web.ui.validator.Validator;

@Controller
@RequestMapping(value = "/signup")
public class SignUpController {

    private static final String VIEW_SINGNUP_START = "signup/start";
    private static final String VIEW_SINGNUP_REGISTER = "signup/register";
    private static final String VIEW_SINGNUP_END = "signup/end";
    private static final String ATTRIBUTE_SESSION_PHONE = "phone";
    private static final String PARAM_ERROR_BAD_PHONE = "bad_phone";
    private static final String PARAM_ERROR_DUPLICATED_USER = "duplicated_user";
    private static final String PARAM_ERROR_INTERNAL = "internal_error";
    private static final String PARAM_ERROR_NO_SEND_SMS = "not_send_sms";
    private static final String INCORECT_PASSWORD = "incorect_password";
    private static final String PARAM_ERROR_NO_SEND_EMAIL = "not_send_email";
    private static final String PARAM_ERROR_INVALIDE_EMAIL = "invalide_email";
    private static final String PARAM_ERROR_NOT_LINK_ACCOUNT = "not_link";

    private static final String MSG_BAD_PHONE = "message.registration.invalid.phone";
    private static final String MSG_DUPLICATED_USER = "message.registration.user.duplicated";
    private static final String MSG_NO_SEND_SMS = "message.registration.failed.send.sms";
    private static final String MSG_NO_SEND_EMAIL = "message.registration.failed.send.email";
    private static final String MSG_INCORECT_PASSWORD = "message.auth.invalid.password";
    private static final String MSG_NOT_LINK_ACCOUNT = "message.registration.failed.link.sn";
    private static final String MSG_NOT_UNLINK_ACCOUNT = "message.registration.failed.unlink.sn";
    private static final String MSG_BAD_EMAIL = "message.registration.invalid.email";

    private static final byte PLUS_INDEX = 1;
    private static final String PARAM_FACEBOOK_ACCOUNT = "facebook";
    private static final String PARAM_VKONTACTE_ACCOUNT = "vkontakte";
    private final Validator phoneValidator = new PhoneValidator();
    private final Validator emailValidator = new EmailValidator();
    private final Logger log = LogManager.getLogger(getClass());

    @Autowired
    @Qualifier("authenticationManager")
    private AuthenticationManager authenticationManager;

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private UserService userService;

    @Autowired
    private MessageTemplateService messageTemplateService;

    @RequestMapping(value = "/start", method = RequestMethod.GET)
    public String start(ModelMap model, HttpServletRequest request, @RequestParam(value = "error", required = false) String error) {
         if (error != null) {
            if (error.equals(PARAM_ERROR_BAD_PHONE)) {
                model.addAttribute("errorphone", messageTemplateService.getMessage(MSG_BAD_PHONE));
            }
            if (error.equals(PARAM_ERROR_DUPLICATED_USER)) {
                model.addAttribute("errorphone", messageTemplateService.getMessage(MSG_DUPLICATED_USER));
            }
            if (error.equals(PARAM_ERROR_NO_SEND_SMS)) {
                model.addAttribute("notsendsms", messageTemplateService.getMessage(MSG_NO_SEND_SMS));
            }
            model.addAttribute("error", error);
            model.addAttribute("checkedtos","checked='on'");
        }
        return VIEW_SINGNUP_START;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String registerPost(ModelMap model, HttpServletRequest request, @RequestParam(value = "phone", required = true) String phone,
            @RequestParam(value = "countryCode", required = true) String countryCode,
            @RequestParam(value = "tos", required = true) String tos) {
        String fullPhone = countryCode.concat(phone);
        if (StringUtils.isBlank(phone) || !phoneValidator.isValid(fullPhone)) {
            return startRedirect(PARAM_ERROR_BAD_PHONE);
        }
        HttpSession session = request.getSession(true);
        session.setAttribute(ATTRIBUTE_SESSION_PHONE, fullPhone);
        try {
            registrationService.createUser(fullPhone);
        } catch (DuplicatedUserException e) {
            log.error(e.getMessage());
            return startRedirect(PARAM_ERROR_DUPLICATED_USER);
        } catch (SmsMessagingException e) {
            log.error(e.getMessage());
            return startRedirect(PARAM_ERROR_NO_SEND_SMS);
        }
        return VIEW_SINGNUP_REGISTER;
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String registerGet(ModelMap model, @RequestParam(value = "error", required = false) String error) {
        if (error.equals(INCORECT_PASSWORD)) {
            model.addAttribute("error", messageTemplateService.getMessage(MSG_INCORECT_PASSWORD));
        }
        return VIEW_SINGNUP_REGISTER;
    }

    @RequestMapping(value = "/end", method = RequestMethod.POST)
    public String end(ModelMap model, HttpServletRequest request, @RequestParam(value = "password", required = true) String password) {

        HttpSession session = request.getSession(true);
        String phone = (String) session.getAttribute("phone");
        if (phone == null) {
            return startRedirect(null);
        }
        try {
            registrationService.activateUser(phone, password);
        } catch (IncorrectPasswordException e) {
            log.error(e,e);
            return registerRedirect(INCORECT_PASSWORD);
        }
        authenticatUser(request, phone, password);
        return VIEW_SINGNUP_END;
    }

    @RequestMapping(value = "/end", method = RequestMethod.GET)
    public String endGet(ModelMap model, @RequestParam(value = "error", required = false) String error) {
        if (error != null) {
            if (error.equals(PARAM_ERROR_NOT_LINK_ACCOUNT)) {
                model.addAttribute("errorlink", messageTemplateService.getMessage(MSG_NOT_LINK_ACCOUNT));
            }
            if (error.equals(PARAM_ERROR_NO_SEND_EMAIL)) {
                model.addAttribute("errormail", messageTemplateService.getMessage(MSG_NO_SEND_EMAIL));
            }
            if (error.equals(PARAM_ERROR_INVALIDE_EMAIL)) {
                model.addAttribute("errormail", messageTemplateService.getMessage(MSG_BAD_EMAIL));
            }
            model.addAttribute("error", error);
        }
        
        User user = userService.findUser(SecurityContextHolder.getContext().getAuthentication().getName());
        List<UserSocialAccount> accounts = user.getAccounts();
        for (UserSocialAccount a : accounts) {
            model.addAttribute(a.getType().toString(), true);
        }
        return VIEW_SINGNUP_END;
    }

    @RequestMapping(value = "/linkfacebook", method = RequestMethod.GET)
    public String linkFacebook(ModelMap model, @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "error", required = false) String error) {

        if (StringUtils.isNotBlank(error)) {
            return endRedirect(PARAM_ERROR_NOT_LINK_ACCOUNT);
        }
        if (StringUtils.isBlank(code)) {
            return endRedirect(PARAM_ERROR_NOT_LINK_ACCOUNT);
        }
        User user = userService.findUser(SecurityContextHolder.getContext().getAuthentication().getName());
        userService.attachAccount(user.getId(), SocialNetworkType.FACEBOOK, code);
        return endRedirect(null);
    }

    @RequestMapping(value = "/linkvkontakte", method = RequestMethod.GET)
    public String linkVkontakte(ModelMap model, @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "error", required = false) String errorVkontakte) {
        if (StringUtils.isNotBlank(errorVkontakte)) {
            return endRedirect(PARAM_ERROR_NOT_LINK_ACCOUNT);
        }
        if (StringUtils.isBlank(code)) {
            return endRedirect(PARAM_ERROR_NOT_LINK_ACCOUNT);
        }
        User user = userService.findUser(SecurityContextHolder.getContext().getAuthentication().getName());
        userService.attachAccount(user.getId(), SocialNetworkType.VKONTAKTE, code);
        return endRedirect(null);
    }

    @RequestMapping(value = "/unlinkaccount", method = RequestMethod.GET)
    public String unlinkSocialAccount(ModelMap model, @RequestParam(value = "account", required = true) String account) {

        User user = userService.findUser(SecurityContextHolder.getContext().getAuthentication().getName());
        List<UserSocialAccount> accounts = user.getAccounts();
        if (account.equals(PARAM_FACEBOOK_ACCOUNT)) {
            userService.detachAccount(user.getId(), SocialNetworkType.FACEBOOK);
        }
        if (account.equals(PARAM_VKONTACTE_ACCOUNT)) {
            userService.detachAccount(user.getId(), SocialNetworkType.VKONTAKTE);
        }
        return endRedirect(null);
    }

    @RequestMapping(value = "/sendmail", method = RequestMethod.POST)
    public String sendForConfirmMail(ModelMap model, @RequestParam(value = "email", required = true) String email) {
        if (!emailValidator.isValid(email)) {
            return endRedirect(PARAM_ERROR_INVALIDE_EMAIL);
        }
        try {
            User user = userService.findUser(SecurityContextHolder.getContext().getAuthentication().getName());
            userService.updateEmail(user.getId(), email);
        } catch (EmailMessagingException e) {
            log.error(e,e);
            return endRedirect(PARAM_ERROR_NO_SEND_EMAIL);
        }
        model.addAttribute("email_success", "Activation email was sent to the specified address. ");
        User user = userService.findUser(SecurityContextHolder.getContext().getAuthentication().getName());
        List<UserSocialAccount> accounts = user.getAccounts();
        for (UserSocialAccount a : accounts) {
            model.addAttribute(a.getType().toString(), true);
        }
        return VIEW_SINGNUP_END;
    }

    private void authenticatUser(HttpServletRequest request, String phone, String password) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(phone, password);
        WebAuthenticationDetails details = new WebAuthenticationDetails(request);
        authenticationToken.setDetails(details);
        Authentication fullauth = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(fullauth);
    }

    private String startRedirect(String errorParam) {
        if (errorParam != null) {
            return "redirect:/signup/start".concat("?error=" + errorParam);
        }
        return "redirect:/signup/start";
    }

    private String endRedirect(String errorParam) {
        if (errorParam != null) {
            return "redirect:/signup/end".concat("?error=" + errorParam);
        }
        return "redirect:/signup/end";
    }

    private String registerRedirect(String errorParam) {
        if (errorParam != null) {
            return "redirect:/signup/register".concat("?error=" + errorParam);
        }
        return "redirect:/signup/register";
    }
}
