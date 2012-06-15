package com.redwerk.likelabs.web.ui.controller;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.redwerk.likelabs.infrastructure.sn.FacebookGateway;
import com.redwerk.likelabs.application.UserService;
import com.redwerk.likelabs.domain.model.user.User;
import com.redwerk.likelabs.domain.model.SocialNetworkType;
import com.redwerk.likelabs.web.ui.security.Authenticator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import java.io.IOException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.codec.binary.Base64;
import com.redwerk.likelabs.domain.model.user.exception.AccountNotFoundException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.redwerk.likelabs.application.template.MessageTemplateService;


@Controller
@RequestMapping(value = "/fb")
public class FBController {
    
    private static final String VIEW_FB_LOGIN = "fb/fb_login";

    private static final String PARAM_ACCESS_TOKEN = "oauth_token";
    private static final String REDIRECT_CANCEL = "http://facebook.com";
    private static final String REDIRECT_INDEX = "redirect:/index";    
    
    private static final String PARAM_SOCIAL_TYPE = "socialType";
    private static final String CLIENT_ID = "app.facebook.clientid";    
    
    private static final Pattern USER_DATA_PATTERN = Pattern.compile("oauth_token\":\"(\\w+).*user_id\":\"(\\d+)");
    
   
    private final Logger log = LogManager.getLogger(getClass());

    @Autowired
    private Authenticator authentificator;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private MessageTemplateService messageTemplateService;

    @RequestMapping(value = {"/", ""}, method = RequestMethod.POST)
    public String requestPost(ModelMap model, @RequestParam(value = "signed_request", required = false) String encodedData, HttpSession session, HttpServletResponse response, HttpServletRequest request) {
        UserSocialAccountData fbUserData = getUserData(encodedData);
        if (fbUserData == null) {            
            return VIEW_FB_LOGIN;
        }
        session.setAttribute(PARAM_SOCIAL_TYPE, SocialNetworkType.FACEBOOK.toString());
        session.setAttribute(PARAM_ACCESS_TOKEN, fbUserData.getAccessToken());
        authentificateUser(SocialNetworkType.FACEBOOK, fbUserData.getUserId(), request);
        return REDIRECT_INDEX;
    }

    @RequestMapping(value = {"/", ""}, method = RequestMethod.GET)
    public void requestGet(ModelMap model, HttpSession session, @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "error", required = false) String error, HttpServletResponse response) {
        
        try {
        if (StringUtils.isNotBlank(error) || StringUtils.isBlank(code)) {
            response.sendRedirect(REDIRECT_CANCEL);
            return;
        }
        response.sendRedirect("http://apps.facebook.com/" + messageTemplateService.getMessage(CLIENT_ID));
        } catch (IOException e) {            
            throw new RuntimeException(e);
        }
    }

    @RequestMapping(value = {"/attach"}, method = RequestMethod.GET)
    public String attachFBUser(ModelMap model, HttpSession session, HttpServletRequest request) {
        if (session.getAttribute(PARAM_ACCESS_TOKEN) != null) {
            String acessToken = session.getAttribute(PARAM_ACCESS_TOKEN).toString();
            long userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());            
            userService.attachAccount(userId, new FacebookGateway().getUserAccountByAccessToken(acessToken));
        }
        return REDIRECT_INDEX;
    }

    private UserSocialAccountData getUserData(String encodedData) {

        String encodedJSON = encodedData.split("\\.", 2)[1];
        byte[] decodedArray = Base64.decodeBase64(encodedJSON);
        String decodedString = new String(decodedArray);
        Matcher matcher = USER_DATA_PATTERN.matcher(decodedString);
        if (matcher.find()) {
            return new UserSocialAccountData(matcher.group(2), matcher.group(1));
        }
        return null;
    }    
       
    private void authentificateUser(SocialNetworkType snType, String socialAccountId, HttpServletRequest request) {
        User user = null;
        try {            
            user = userService.getUser(snType, socialAccountId);
        } catch (AccountNotFoundException ex) {
            // do nothing
            log.info("Social account not attached to any user");
        }
        if (user != null) {
            authentificator.authenticateUser(request, user);
        }
    }
    
    private class UserSocialAccountData {
        private final String userId;
        private final String accessToken;

        public UserSocialAccountData(String userId, String accessToken) { 
            this.userId = userId;
            this.accessToken = accessToken;        
        }

        public String getUserId() {        
            return userId;
        }

        public String getAccessToken() {        
            return accessToken;
        }
    }
}
