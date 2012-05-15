package com.redwerk.likelabs.web.ui.controller;

import com.redwerk.likelabs.application.RegistrationService;
import com.redwerk.likelabs.application.UserService;
import com.redwerk.likelabs.application.impl.registration.exception.AbsentSocialAccountException;
import com.redwerk.likelabs.application.impl.registration.exception.PageAccessLevelException;
import com.redwerk.likelabs.application.impl.registration.exception.AbsentCompanyException;
import com.redwerk.likelabs.application.messaging.MessageTemplateService;
import com.redwerk.likelabs.application.sn.GatewayFactory;
import com.redwerk.likelabs.domain.model.SocialNetworkType;
import com.redwerk.likelabs.domain.model.user.User;
import com.redwerk.likelabs.domain.model.user.UserSocialAccount;
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
@RequestMapping(value = "/admincompany")
public class CompanyAdminController {

    private static final String VIEW_ADMIN_PROFILE = "admincompany/profile";
    private static final String VIEW_SUCCESS = "admincompany/successactivate";
    private static final String MSG_INCORECT_PASSWORD = "message.auth.invalid.password";
    private static final String MSG_NOT_LINK_ACCOUNT = "message.registration.failed.link.sn";
    private static final String MSG_NOT_UNLINK_ACCOUNT = "message.registration.failed.unlink.sn";
    private static final String MSG_NOT_ADMIN = "message.registration.invalid.activate.admin";
    
    private static final String PARAM_FACEBOOK_ACCOUNT = "facebook";
    private static final String PARAM_VKONTACTE_ACCOUNT = "vkontakte";
    private static final String PARAM_ERROR_NOT_LINK_ACCOUNT = "not_link";
    private static final String PARAM_ERROR_NOT_ADMIN = "not_admin";
    private static final String PARAM_ERROR_ALREADY_ACTIVE = "already_active";

    private static final String PARAM_SESSION_USERID = "userId";

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


    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String profileGet(ModelMap model, HttpServletRequest request, @RequestParam(value = "error", required = false) String error) {
        HttpSession session = request.getSession(true);
        Long id = (Long)session.getAttribute(PARAM_SESSION_USERID);
        User user = userService.getUser(id);
        if (user.isActive()) {
            authenticatUser(request,user.getPhone(), user.getPassword());
            return "redirect:/admincompany/success?error=" + PARAM_ERROR_ALREADY_ACTIVE;
        }
        if (error != null) {
            if (error.equals(PARAM_ERROR_NOT_LINK_ACCOUNT)) {
                model.addAttribute("errorlink", messageTemplateService.getMessage(MSG_NOT_LINK_ACCOUNT));
            }
            if (error.equals(PARAM_ERROR_NOT_ADMIN)) {
                model.addAttribute("error_not_admin", messageTemplateService.getMessage(MSG_NOT_ADMIN));
            }
        }
        List<UserSocialAccount> accounts = user.getAccounts();
        for (UserSocialAccount a : accounts) {
            model.addAttribute(a.getType().toString(), true);
        }
        return VIEW_ADMIN_PROFILE;
    }

    @RequestMapping(value = "/profile", method = RequestMethod.POST)
    public String profilePost(ModelMap model, HttpServletRequest request) {

        try {
            HttpSession session = request.getSession(true);
            Long id = (Long)session.getAttribute(PARAM_SESSION_USERID);
            User user = userService.getUser(id);
            registrationService.activateCompanyAdmin(user.getId());
            authenticatUser(request,user.getPhone(), user.getPassword());
        } catch (AbsentSocialAccountException e) {
            return profileRedirect(PARAM_ERROR_NOT_ADMIN);
        } catch (AbsentCompanyException e) {
            return profileRedirect(PARAM_ERROR_NOT_ADMIN);
        } catch (PageAccessLevelException e) {
            return profileRedirect(PARAM_ERROR_NOT_ADMIN);
        }
        return "redirect:/admincompany/success";
    }


    @RequestMapping(value = "/success", method = RequestMethod.GET)
    public String success(ModelMap model, @RequestParam(value = "error", required = false) String error) {
        if (error != null) model.addAttribute(PARAM_ERROR_ALREADY_ACTIVE, true);
        return VIEW_SUCCESS;
    }

    @RequestMapping(value = "/linkfacebook", method = RequestMethod.GET)
    public String linkFacebook(ModelMap model, HttpServletRequest request,
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "error", required = false) String error) {

        if (StringUtils.isNotBlank(error)) {
            return profileRedirect(PARAM_ERROR_NOT_LINK_ACCOUNT);
        }
        if (StringUtils.isBlank(code)) {
            return profileRedirect(PARAM_ERROR_NOT_LINK_ACCOUNT);
        }
        HttpSession session = request.getSession(true);
        Long id = (Long)session.getAttribute(PARAM_SESSION_USERID);
        User user = userService.getUser(id);
        userService.attachAccount(user.getId(), SocialNetworkType.FACEBOOK, code);
        return profileRedirect(null);
    }


    @RequestMapping(value = "/linkvkontakte", method = RequestMethod.GET)
    public String linkVkontakte(ModelMap model, HttpServletRequest request,
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "error", required = false) String errorVkontakte) {
        if (StringUtils.isNotBlank(errorVkontakte)) {
            return profileRedirect(PARAM_ERROR_NOT_LINK_ACCOUNT);
        }
        if (StringUtils.isBlank(code)) {
            return profileRedirect(PARAM_ERROR_NOT_LINK_ACCOUNT);
        }
        HttpSession session = request.getSession(true);
            Long id = (Long)session.getAttribute(PARAM_SESSION_USERID);
            User user = userService.getUser(id);
        userService.attachAccount(user.getId(), SocialNetworkType.VKONTAKTE, code);
        return profileRedirect(null);
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
        return profileRedirect(null);
    }


    private String profileRedirect(String errorParam) {
        if (errorParam != null) {
            return "redirect:/admincompany/profile".concat("?error=" + errorParam);
        }
        return "redirect:/admincompany/profile";
    }

    private void authenticatUser(HttpServletRequest request, String phone, String password) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(phone, password);
        WebAuthenticationDetails details = new WebAuthenticationDetails(request);
        authenticationToken.setDetails(details);
        Authentication fullauth = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(fullauth);
    }
}
