package com.redwerk.likelabs.web.ui.controller;

import com.redwerk.likelabs.application.RegistrationService;
import com.redwerk.likelabs.application.UserService;
import com.redwerk.likelabs.application.impl.registration.exception.AbsentSocialAccountException;
import com.redwerk.likelabs.application.impl.registration.exception.PageAccessLevelException;
import com.redwerk.likelabs.application.impl.registration.exception.AbsentCompanyException;
import com.redwerk.likelabs.application.template.MessageTemplateService;
import com.redwerk.likelabs.domain.service.sn.GatewayFactory;
import com.redwerk.likelabs.domain.model.SocialNetworkType;
import com.redwerk.likelabs.domain.model.user.User;
import com.redwerk.likelabs.domain.model.user.UserSocialAccount;
import com.redwerk.likelabs.domain.model.user.exception.AccountNotExistsException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

@Controller
@RequestMapping(value = "/companyadmin/activate")
public class CompanyAdminActivationController {

    private static final String VIEW_END_ACTIVATE = "companyadmin/end_activate";
    private static final String VIEW_SUCCESS_ACTIVATE = "companyadmin/success_activate";
    private static final String VIEW_START_ACTIVATE = "companyadmin/activate";

    private static final String MSG_INCORECT_PASSWORD = "message.auth.invalid.password";
    private static final String MSG_NOT_LINK_ACCOUNT = "message.registration.failed.link.sn";
    private static final String MSG_NOT_UNLINK_ACCOUNT = "message.registration.failed.unlink.sn";
    private static final String MSG_NOT_ADMIN = "message.registration.invalid.activate.admin";
    
    private static final String PARAM_FACEBOOK_ACCOUNT = "facebook";
    private static final String PARAM_VKONTACTE_ACCOUNT = "vkontakte";
    private static final String PARAM_ERROR_NOT_LINK_ACCOUNT = "not_link";
    private static final String PARAM_ERROR_NOT_UNLINK_ACCOUNT = "not_unlink";
    private static final String PARAM_ERROR_NOT_ADMIN = "not_admin";
    private static final String PARAM_ERROR_ALREADY_ACTIVE = "already_active";

    private static final String PARAM_SESSION_USERID = "userId";
    private static final String PARAM_SESSION_PASSWORD = "password";

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

    @Autowired
    GatewayFactory gatewayFactory;


    @RequestMapping(method = RequestMethod.GET)
    public String activateAdminGet(ModelMap model,
                                   @RequestParam("userId") Long userId,
                                   @RequestParam("code") String confirmCode) {
        try {
            User user = userService.getUser(userId);
            if (user == null) {
                model.addAttribute("errorcode", true);
                return VIEW_START_ACTIVATE;
            }
            if (registrationService.validateAdminCode(userId, confirmCode)) {
                if (user.isActive()) {
                    return "redirect:/companadminy/activate/success?error=already_active";
                }
                model.addAttribute("activatecode", confirmCode);
                model.addAttribute("id", userId);
            } else {
                model.addAttribute("errorcode", true);
            }
        } catch (IllegalStateException e) {
            log.error(e,e);
            model.addAttribute("errorcode", true);
        }
        return VIEW_START_ACTIVATE;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String activateAdminPost(ModelMap model, HttpSession session,
                                    @RequestParam("userId") Long userId,
                                    @RequestParam("code") String confirmCode,
                                    @RequestParam("password") String password) {

        try {
            User user = userService.getUser(userId);
            if (registrationService.validateAdminPassword(user.getId(), password) && registrationService.validateAdminCode(userId, confirmCode)) {
                session.setAttribute(PARAM_SESSION_USERID, userId);
                session.setAttribute(PARAM_SESSION_PASSWORD, password);
                return "redirect:/companyadmin/activate/end";
            }
            model.addAttribute("error", messageTemplateService.getMessage(MSG_INCORECT_PASSWORD));
            model.addAttribute("activatecode", confirmCode);
            model.addAttribute("id", userId);
        } catch (Exception e) {
            log.error(e,e);
        }
        return VIEW_START_ACTIVATE;
    }

    @RequestMapping(value = "/end", method = RequestMethod.GET)
    public String end(ModelMap model, HttpSession session, HttpServletRequest request, @RequestParam(value = "error", required = false) String error) {
        Long id = (Long)session.getAttribute(PARAM_SESSION_USERID);
        if (id == null) {
            return "redirect:/";
        }
        User user = userService.getUser(id);
        if (user.isActive()) {
            authenticateUser(request, user.getId(), user.getPassword());
            return "redirect:/companyadmin/activate/success?error=" + PARAM_ERROR_ALREADY_ACTIVE;
        }
        if (error != null) {
            if (error.equals(PARAM_ERROR_NOT_LINK_ACCOUNT)) {
                model.addAttribute("errorlink", messageTemplateService.getMessage(MSG_NOT_LINK_ACCOUNT));
            }
            if (error.equals(PARAM_ERROR_NOT_UNLINK_ACCOUNT)) {
                model.addAttribute("errorUnlink", messageTemplateService.getMessage(MSG_NOT_UNLINK_ACCOUNT));
            }
            if (error.equals(PARAM_ERROR_NOT_ADMIN)) {
                model.addAttribute("error_not_admin", messageTemplateService.getMessage(MSG_NOT_ADMIN));
            }
        }
        List<UserSocialAccount> accounts = user.getAccounts();
        for (UserSocialAccount a : accounts) {
            model.addAttribute(a.getType().toString(), true);
        }
        return VIEW_END_ACTIVATE;
    }

    @RequestMapping(value = "/finish", method = RequestMethod.GET)
    public String finish(ModelMap model, HttpSession session, HttpServletRequest request) {

        try {
            Long id = (Long)session.getAttribute(PARAM_SESSION_USERID);
            if (id == null) {
                return "redirect:/";
            }
            User user = userService.getUser(id);
            if (user == null) {
                return "redirect:/";
            }
            if (!user.getPassword().equals((String)session.getAttribute(PARAM_SESSION_PASSWORD))) {
                return "redirect:/";
            }
            registrationService.activateCompanyAdmin(user.getId());
            authenticateUser(request, user.getId(), user.getPassword());
        } catch (AbsentSocialAccountException e) {
            return endRedirect(PARAM_ERROR_NOT_ADMIN);
        } catch (AbsentCompanyException e) {
            return endRedirect(PARAM_ERROR_NOT_ADMIN);
        } catch (PageAccessLevelException e) {
            return endRedirect(PARAM_ERROR_NOT_ADMIN);
        }
        return "redirect:/companyadmin/activate/success";
    }


    @RequestMapping(value = "/success", method = RequestMethod.GET)
    public String success(ModelMap model, @RequestParam(value = "error", required = false) String error) {

        if (error != null) model.addAttribute(PARAM_ERROR_ALREADY_ACTIVE, true);
        return VIEW_SUCCESS_ACTIVATE;
    }

    @RequestMapping(value = "/linkfacebook", method = RequestMethod.GET)
    public String linkFacebook(ModelMap model, HttpServletRequest request,
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "error", required = false) String error) {

        if (StringUtils.isNotBlank(error)) {
            return endRedirect(PARAM_ERROR_NOT_LINK_ACCOUNT);
        }
        if (StringUtils.isBlank(code)) {
            return endRedirect(PARAM_ERROR_NOT_LINK_ACCOUNT);
        }
        HttpSession session = request.getSession(true);
        Long id = (Long)session.getAttribute(PARAM_SESSION_USERID);
        User user = userService.getUser(id);
        userService.attachAccount(user.getId(), SocialNetworkType.FACEBOOK, code);
        return endRedirect(null);
    }


    @RequestMapping(value = "/linkvkontakte", method = RequestMethod.GET)
    public String linkVkontakte(ModelMap model, HttpServletRequest request,
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "error", required = false) String errorVkontakte) {
        if (StringUtils.isNotBlank(errorVkontakte)) {
            return endRedirect(PARAM_ERROR_NOT_LINK_ACCOUNT);
        }
        if (StringUtils.isBlank(code)) {
            return endRedirect(PARAM_ERROR_NOT_LINK_ACCOUNT);
        }
        HttpSession session = request.getSession(true);
            Long id = (Long)session.getAttribute(PARAM_SESSION_USERID);
            User user = userService.getUser(id);
        userService.attachAccount(user.getId(), SocialNetworkType.VKONTAKTE, code);
        return endRedirect(null);
    }


    @RequestMapping(value = "/unlinkaccount", method = RequestMethod.GET)
    public String unlinkSocialAccount(ModelMap model, @RequestParam(value = "account", required = true) String account) {

        User user = userService.getUser(Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName()));
        try {
            if (account.equals(PARAM_FACEBOOK_ACCOUNT)) {
                userService.detachAccount(user.getId(), SocialNetworkType.FACEBOOK);
            }
            if (account.equals(PARAM_VKONTACTE_ACCOUNT)) {
                userService.detachAccount(user.getId(), SocialNetworkType.VKONTAKTE);
            }
        } catch (IllegalArgumentException e) {
            log.error(e,e);
            return endRedirect(PARAM_ERROR_NOT_UNLINK_ACCOUNT);
        } catch (AccountNotExistsException e) {
            log.error(e,e);
            return endRedirect(PARAM_ERROR_NOT_UNLINK_ACCOUNT);
        }
        return endRedirect(null);
    }


    private String endRedirect(String errorParam) {
        if (errorParam != null) {
            return "redirect:/companyadmin/activate/end".concat("?error=" + errorParam);
        }
        return "redirect:/companyadmin/activate/end";
    }

    private void authenticateUser(HttpServletRequest request, Long userId, String password) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(String.valueOf(userId), password);
        WebAuthenticationDetails details = new WebAuthenticationDetails(request);
        authenticationToken.setDetails(details);
        Authentication fullauth = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(fullauth);
    }
}
