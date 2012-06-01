package com.redwerk.likelabs.web.ui.controller;

import com.redwerk.likelabs.application.CompanyService;
import com.redwerk.likelabs.application.RegistrationService;
import com.redwerk.likelabs.application.UserService;
import com.redwerk.likelabs.application.dto.company.CompanyReportItem;
import com.redwerk.likelabs.application.impl.registration.exception.AbsentSocialAccountException;
import com.redwerk.likelabs.application.impl.registration.exception.PageAccessLevelException;
import com.redwerk.likelabs.application.impl.registration.exception.AbsentCompanyException;
import com.redwerk.likelabs.application.template.MessageTemplateService;
import com.redwerk.likelabs.domain.model.query.Pager;
import com.redwerk.likelabs.domain.model.user.User;
import com.redwerk.likelabs.domain.model.user.exception.UserNotFoundException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
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
    private static final String MSG_NOT_ADMIN = "message.registration.invalid.admin.activate";
    private static final String MSG_NOT_ATTACHED_ACCOUNT = "message.registration.invalid.admin.not.account";
    
    private static final String RESPONSE_KEY_SUCCESS = "success";
    private static final String RESPONSE_KEY_MESSAGE = "message";
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
    private CompanyService companyService;

    @Autowired
    private RememberMeServices rememberMeServices;

    @RequestMapping(method = RequestMethod.GET)
    public String activateAdminGet(ModelMap model, HttpSession session, 
                                   @RequestParam("userId") Long userId,
                                   @RequestParam("code") String confirmCode) {
        try {
            User user = userService.getUser(userId);
            if (user == null) {
                model.addAttribute("errorcode", true);
                return VIEW_START_ACTIVATE;
            }
            if (registrationService.validateAdminCode(userId, confirmCode)) {
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
    public String activateAdminPost(ModelMap model, HttpSession session, HttpServletRequest request, HttpServletResponse response,
                                    @RequestParam("userId") Long userId,
                                    @RequestParam("code") String confirmCode,
                                    @RequestParam("password") String password) {

        try {
            User user = userService.getUser(userId);
            if (registrationService.validateAdminPassword(user.getId(), password) && registrationService.validateAdminCode(userId, confirmCode)) {
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                if (auth != null){
                    new SecurityContextLogoutHandler().logout(request, response, auth);
                    rememberMeServices.loginFail(request, response);
                }
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
            model.put(RESPONSE_KEY_SUCCESS, false);
            List<CompanyReportItem> report = companyService.getCompaniesForAdmin(user.getId(), Pager.ALL_RECORDS).getItems();
            if (!report.isEmpty()) {
                model.put("companyName", report.get(0).getCompany().getName());
            }
            return VIEW_SUCCESS_ACTIVATE;
        }
        if (error != null) {
            if (error.equals(PARAM_ERROR_NOT_ADMIN)) {
                model.addAttribute("error_not_admin", messageTemplateService.getMessage(MSG_NOT_ADMIN));
            }
        }
        return VIEW_END_ACTIVATE;
    }

    @RequestMapping(value = "/finish", method = RequestMethod.POST)
    public String finish(ModelMap model, HttpSession session, HttpServletRequest request) {

        try {
            Long userId = (Long)session.getAttribute(PARAM_SESSION_USERID);
            if (userId == null) {
                return "redirect:/";
            }
            User user = userService.getUser(userId);
            if (!user.getPassword().equals((String)session.getAttribute(PARAM_SESSION_PASSWORD))) {
                return "redirect:/";
            }
            registrationService.activateCompanyAdmin(user.getId());
            authenticateUser(request, user.getId(), user.getPassword());
            List<CompanyReportItem> report = companyService.getCompaniesForAdmin(user.getId(), Pager.ALL_RECORDS).getItems();
            if (!report.isEmpty()) {
                model.put("companyName", report.get(0).getCompany().getName());
            }
            model.put(RESPONSE_KEY_SUCCESS, true);
        } catch (UserNotFoundException e) {
            log.error(e,e);
            return "redirect:/";
        } catch (AbsentSocialAccountException e) {
            model.put(RESPONSE_KEY_SUCCESS, false);
            model.put(RESPONSE_KEY_MESSAGE, messageTemplateService.getMessage(MSG_NOT_ATTACHED_ACCOUNT));
            log.error(e,e);
            return VIEW_END_ACTIVATE;
        } catch (PageAccessLevelException e) {
            model.put(RESPONSE_KEY_SUCCESS, false);
            model.put(RESPONSE_KEY_MESSAGE, messageTemplateService.getMessage(MSG_NOT_ADMIN));
            log.error(e,e);
            return VIEW_END_ACTIVATE;
        } catch (AbsentCompanyException e) {
            log.error(e,e);
            return "redirect:/";
        }
        return VIEW_SUCCESS_ACTIVATE;
    }

    private void authenticateUser(HttpServletRequest request, Long userId, String password) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(String.valueOf(userId), password);
        WebAuthenticationDetails details = new WebAuthenticationDetails(request);
        authenticationToken.setDetails(details);
        Authentication fullauth = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(fullauth);
    }
}
