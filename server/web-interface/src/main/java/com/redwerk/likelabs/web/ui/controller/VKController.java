package com.redwerk.likelabs.web.ui.controller;

import com.redwerk.likelabs.application.UserService;
import com.redwerk.likelabs.domain.model.SocialNetworkType;
import com.redwerk.likelabs.domain.model.user.User;
import com.redwerk.likelabs.domain.model.user.UserSocialAccount;
import com.redwerk.likelabs.domain.model.user.exception.AccountNotFoundException;
import com.redwerk.likelabs.domain.service.sn.GatewayFactory;
import com.redwerk.likelabs.infrastructure.security.AuthorityRole;
import com.redwerk.likelabs.web.ui.security.Authenticator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value="/vk")
public class VKController {
    
    private final Logger log = LogManager.getLogger(getClass());
    
    private static final String SOCIAL_TYPE = "socialType";
    
    private static final String VIEWER_ID = "viewer_id";

    @Autowired
    private UserService userService;
 
    @Autowired
    private Authenticator authentificator;
    
    @RequestMapping(value={"/", ""}, method = RequestMethod.GET)
    public String requestGET(@RequestParam(value = "viewer_id", required = true) String socialAccountId, HttpSession session, HttpServletRequest request) {
        session.setAttribute(VIEWER_ID, socialAccountId);
        session.setAttribute(SOCIAL_TYPE, SocialNetworkType.VKONTAKTE.toString());
        User user = null;
        try {
            user = userService.getUser(SocialNetworkType.VKONTAKTE, socialAccountId);
        } catch (AccountNotFoundException ex) {
            log.info("Social account not attached to any user");
        }
        if(user != null){
            authentificator.authenticateUser(request, user);
        }
        return "redirect:/index";
    }
    
}
