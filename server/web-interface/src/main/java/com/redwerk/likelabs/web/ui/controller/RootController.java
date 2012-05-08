package com.redwerk.likelabs.web.ui.controller;

import com.redwerk.likelabs.application.RegistrationService;
import com.redwerk.likelabs.application.UserService;
import com.redwerk.likelabs.application.impl.registration.exception.NotConfirmMailException;
import com.redwerk.likelabs.domain.model.user.User;
import javax.servlet.http.HttpServletRequest;
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
@RequestMapping(value = "/")
public class RootController {

    private static final String ACTION_PARAM = "action";
    private static final String ACTION_PARAM_NEED_LOGIN = "need_login";
    private static final String ACTION_PARAM_AUTH_FAILED = "auth_failed";
    private static final String VIEW_INDEX = "index";
    private static final String VIEW_SINGNUP_VERIFYMAIL = "signup/verifymail";
    private final Logger log = LogManager.getLogger(getClass());
    @Autowired
    @Qualifier("authenticationManager")
    private AuthenticationManager authenticationManager;
    @Autowired
    RegistrationService registrationService;
    @Autowired
    UserService userService;

    @RequestMapping(value = {"/index", "/", ""}, method = RequestMethod.GET)
    public String index(ModelMap model, @RequestParam(value = ACTION_PARAM, required = false) String action) {

        if (action != null) {
            model.addAttribute(ACTION_PARAM, action);
            log.fatal(action);
        }
        return VIEW_INDEX;
    }

    @RequestMapping(value = "/activatemail", method = RequestMethod.GET)
    public String activateMail(ModelMap model, HttpServletRequest request,
            @RequestParam(value = "id", required = true) String userId,
            @RequestParam(value = "email", required = true) String email,
            @RequestParam(value = "activatecode", required = true) String confirmCode) {

        try {
            long id = Long.parseLong(userId);
            registrationService.confirmEmail(id, email, confirmCode);
            User user = userService.getUser(id);
            authenticatUser(request, user.getPhone(), user.getPassword());
        } catch (NotConfirmMailException e) {
            log.error(e.getMessage());
            model.addAttribute("error", "not verify e-mail: " + email);
        } catch (IllegalStateException e) {
            log.error(e.getMessage());
            model.addAttribute("error", "User with id = " + userId + " is not found");
        } catch (Exception e) {
            log.error(e.getMessage());
            model.addAttribute("error", "Server error? e-mail not activate");
        } finally {
            return VIEW_SINGNUP_VERIFYMAIL;
        }
    }

    private void authenticatUser(HttpServletRequest request, String phone, String password) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(phone, password);
        WebAuthenticationDetails details = new WebAuthenticationDetails(request);
        authenticationToken.setDetails(details);
        Authentication fullauth = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(fullauth);
    }
}
