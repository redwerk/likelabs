package com.redwerk.likelabs.web.ui.controller;

import com.redwerk.likelabs.application.RegistrationService;
import com.redwerk.likelabs.application.UserService;
import com.redwerk.likelabs.application.impl.registration.CodeGenerator;
import com.redwerk.likelabs.application.impl.registration.exception.NotConfirmMailException;
import com.redwerk.likelabs.application.messaging.MessageTemplateService;
import com.redwerk.likelabs.domain.model.user.User;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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

   
    private static final String VIEW_INDEX = "index";
    private static final String VIEW_ACTIVATE_EMAIL = "activatemail";
    private static final String VIEW_TOS = "tos";
    private static final String VIEW_ACTIVATE_ADMIN = "admincompany/activateadmin";

    private static final String MSG_INCORECT_PASSWORD = "message.auth.invalid.password";

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

    @RequestMapping(value = {"/index", "/", ""}, method = RequestMethod.GET)
    public String index(ModelMap model, @RequestParam(value = "error", required = false) String error) {
     
        //TODO index logic
        return VIEW_INDEX;
    }


    @RequestMapping(value = "/tos", method = RequestMethod.GET)
    public String tos(ModelMap model) {
        return VIEW_TOS;
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
            log.error(e,e);
            model.addAttribute("error", true);
        } catch (IllegalStateException e) {
            log.error(e,e);
            model.addAttribute("error", true);
        } finally {
            return VIEW_ACTIVATE_EMAIL;
        }
    }

    @RequestMapping(value = "/activateadmin", method = RequestMethod.GET)
    public String activateAdminGet(ModelMap model, HttpServletRequest request,
            @RequestParam(value = "id", required = true) String userId,
            @RequestParam(value = "activatecode", required = true) String confirmCode) {
        try {
            long id = Long.parseLong(userId);
            User user = userService.getUser(id);
            if (user == null) {
                model.addAttribute("errorcode", true);
                return VIEW_ACTIVATE_ADMIN;
            }
            if (registrationService.validateAdminCode(id, confirmCode)) {
                if (user.isActive()) {
                    return "redirect:/admincompany/success?error=already_active";
                }
                model.addAttribute("activatecode", confirmCode);
                model.addAttribute("id", id);
            } else {
                model.addAttribute("errorcode", true);
            }
        } catch (IllegalStateException e) {
            log.error(e,e);
            model.addAttribute("errorcode", true);
        } finally {
            return VIEW_ACTIVATE_ADMIN;
        }
    }

    @RequestMapping(value = "/activateadmin", method = RequestMethod.POST)
    public String activateAdminPost(ModelMap model, HttpServletRequest request,
            @RequestParam(value = "id", required = true) String userId,
            @RequestParam(value = "activatecode", required = true) String confirmCode,
            @RequestParam(value = "password", required = true) String password) {

        try {
            long id = Long.parseLong(userId);
            User user = userService.getUser(id);
            if (registrationService.validateAdminPassword(user.getId(), password) && registrationService.validateAdminCode(id, confirmCode)) {
                HttpSession session = request.getSession(true);
                session.setAttribute("userId", id);
                return "redirect:/admincompany/profile";
            }
            model.addAttribute("error", messageTemplateService.getMessage(MSG_INCORECT_PASSWORD));
            model.addAttribute("activatecode", confirmCode);
            model.addAttribute("id", id);
        } catch (Exception e) {
            log.error(e,e);
        }
        return VIEW_ACTIVATE_ADMIN;
    }

    @RequestMapping(value = {"/about", "/about/"}, method = RequestMethod.GET)
    public String aboutUs() {

        return "about";
    }

    @RequestMapping(value = {"/faq", "/faq/"}, method = RequestMethod.GET)
    public String faq() {

        return "faq";
    }


    private void authenticatUser(HttpServletRequest request, String phone, String password) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(phone, password);
        WebAuthenticationDetails details = new WebAuthenticationDetails(request);
        authenticationToken.setDetails(details);
        Authentication fullauth = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(fullauth);
    }
}
