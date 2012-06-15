package com.redwerk.likelabs.web.ui.controller;

import com.redwerk.likelabs.application.UserService;
import com.redwerk.likelabs.domain.model.SocialNetworkType;
import com.redwerk.likelabs.domain.model.user.User;
import com.redwerk.likelabs.domain.model.user.UserSocialAccount;
import com.redwerk.likelabs.domain.model.user.exception.AccountNotFoundException;
import com.redwerk.likelabs.domain.service.sn.GatewayFactory;
import com.redwerk.likelabs.web.ui.security.Authenticator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value="/vk")
public class VKController {
    
    private final Logger log = LogManager.getLogger(getClass());
    
    private static final String ACCESS_TOKEN = "access_token";
    
    private static final String SOCIAL_TYPE = "socialType";

    @Autowired
    private UserService userService;
 
    @Autowired
    private Authenticator authentificator;
    
    @Autowired
    private GatewayFactory gatewayFactory;
    
    @RequestMapping(value={"/", ""}, method = RequestMethod.GET)
    public String requestGET(@RequestParam(value = "user_id", required = true) String socialAccountId,
            @RequestParam(value = "access_token", required = true) String accessToken, HttpSession session, HttpServletRequest request) {
        session.setAttribute(ACCESS_TOKEN, accessToken);
        session.setAttribute(SOCIAL_TYPE,SocialNetworkType.VKONTAKTE.toString());
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
    
    @RequestMapping(value={"attach/", "attach"}, method = RequestMethod.GET)
    public String attachAccount(HttpSession session) {
        Authentication  auth = SecurityContextHolder.getContext().getAuthentication();
        String accessToken = (String)session.getAttribute(ACCESS_TOKEN);
        UserSocialAccount sc = gatewayFactory.getGateway(SocialNetworkType.VKONTAKTE).getUserAccountByAccessToken(accessToken);
        userService.attachAccount(Long.parseLong(auth.getName()), sc);
        return "redirect:/index";
    }
    
}
