package com.redwerk.likelabs.web.ui.controller;

import com.redwerk.likelabs.application.RegistrationService;
import com.redwerk.likelabs.application.UserService;
import com.redwerk.likelabs.application.impl.registration.exception.NotConfirmMailException;
import com.redwerk.likelabs.application.template.MessageTemplateService;
import com.redwerk.likelabs.domain.model.user.User;
import com.redwerk.likelabs.domain.model.user.exception.UserNotFoundException;
import com.redwerk.likelabs.infrastructure.security.AuthorityRole;
import com.redwerk.likelabs.web.ui.security.Authenticator;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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

    private static final String VIEW_ACTIVATE_EMAIL = "activatemail";
    private static final String VIEW_TOU = "commons/tou";
    private static final String VIEW_ABOUT = "commons/about";
    private static final String VIEW_GET_STARTED = "commons/get_started";
    private static final String VIEW_HOW_IT_WORKS = "commons/how_it_works";
    private static final String MODEL_ATTR_ERROR = "error";

    private final Logger log = LogManager.getLogger(getClass());

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private UserService userService;

    @Autowired
    private Authenticator authenticator;

    @Autowired
    private MessageTemplateService messageTemplateService;

    @RequestMapping(value = {"/index", "/", ""}, method = RequestMethod.GET)
    public String index(ModelMap model) {

        Authentication  auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().contains(new SimpleGrantedAuthority(AuthorityRole.ROLE_ANONYMOUS.toString()))) {
            return "redirect:/public";
        }
        if (auth.getAuthorities().contains(new SimpleGrantedAuthority(AuthorityRole.ROLE_SYSTEM_ADMIN.toString()))) {
            return "redirect:/administrator";
        }
        if (auth.getAuthorities().contains(new SimpleGrantedAuthority(AuthorityRole.ROLE_COMPANY_ADMIN.toString()))) {
            return "redirect:/companyadmin/" + auth.getName();
        }
        return "redirect:/user/" + auth.getName();
    }

    @RequestMapping(value = "/activatemail", method = RequestMethod.GET)
    public String activateMail(ModelMap model, HttpServletRequest request,
            @RequestParam(value = "id", required = true) String userId,
            @RequestParam(value = "email", required = true) String email,
            @RequestParam(value = "activatecode", required = true) String confirmCode) {

        try {
            long id = Long.parseLong(userId);
            registrationService.confirmEmail(id, email, confirmCode);
            authenticator.authenticateUser(request, userService.getUser(id));
        } catch (NotConfirmMailException e) {
            log.error(e,e);
            model.addAttribute(MODEL_ATTR_ERROR, true);
        } catch (IllegalStateException e) {
            log.error(e,e);
            model.addAttribute(MODEL_ATTR_ERROR, true);
        } catch (UserNotFoundException e) {
            log.error(e,e);
            model.addAttribute(MODEL_ATTR_ERROR, true);
        }
        return VIEW_ACTIVATE_EMAIL;
    }

    @RequestMapping(value = {"/about", "/about/"}, method = RequestMethod.GET)
    public String aboutUs(ModelMap model) {
        model.addAttribute("page", "about_us");
        return VIEW_ABOUT;
    }

    @RequestMapping(value = {"/getsatrted", "/getsatrted/"}, method = RequestMethod.GET)
    public String getStarted(ModelMap model) {
        model.addAttribute("page", "get_started");
        return VIEW_GET_STARTED;
    }

    @RequestMapping(value = {"/howitworks", "/howitworks/"}, method = RequestMethod.GET)
    public String howItWorks(ModelMap model) {
        model.addAttribute("page", "how_it_works");
        return VIEW_HOW_IT_WORKS;
    }

    @RequestMapping(value = {"/tou","/tou/"}, method = RequestMethod.GET)
    public String tos(ModelMap model) {
        model.addAttribute("page", "terms_of_use");
        return VIEW_TOU;
    }
}
