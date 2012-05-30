package com.redwerk.likelabs.web.ui.controller;

import com.redwerk.likelabs.application.UserService;
import com.redwerk.likelabs.application.template.MessageTemplateService;
import com.redwerk.likelabs.domain.model.SocialNetworkType;
import com.redwerk.likelabs.domain.model.user.User;
import com.redwerk.likelabs.domain.model.user.UserSocialAccount;
import com.redwerk.likelabs.domain.model.user.exception.AccountNotExistsException;
import com.redwerk.likelabs.domain.model.user.exception.DuplicatedAccountException;
import com.redwerk.likelabs.domain.model.user.exception.UserNotFoundException;
import com.redwerk.likelabs.domain.service.sn.exception.WrongAccessCodeException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping(value = "/connector")
public class ConnectorSocialNetworkController {

    private static final String VIEW_REDIRECT = "redirect_social_popup";

    private static final String MSG_SN_CONNECT_ERROR = "connect.sn.failed";
    private static final String MSG_SN_DISCONNECT_ERROR = "connect.sn.disconnect.failed";
    private static final String MSG_SN_DUPLICATED_ACCOUNT = "connect.sn.duplicated.account";

    private static final String RESPONSE_KEY_SUCCESS = "success";
    private static final String RESPONSE_KEY_MESSAGE = "message";

    private static final String PARAM_SESSION_USERID = "userId";

    private final Logger log = LogManager.getLogger(getClass());

    @Autowired
    private UserService userService;

    @Autowired
    private MessageTemplateService messageTemplateService;


    @RequestMapping(value = "/facebook", method = RequestMethod.GET)
    public String linkFacebook(ModelMap model, HttpSession session, @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "error", required = false) String error) {

        if (StringUtils.isNotBlank(error) || StringUtils.isBlank(code)) {
            model.put(RESPONSE_KEY_SUCCESS,false);
            model.put(RESPONSE_KEY_MESSAGE,messageTemplateService.getMessage(MSG_SN_CONNECT_ERROR));
            return VIEW_REDIRECT;
        }
        try {
            Long userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
            if (userId  == 0) {
                userId = (Long)session.getAttribute(PARAM_SESSION_USERID);
                if (userId == null) return VIEW_REDIRECT;
            }
            userService.attachAccount(userId, SocialNetworkType.FACEBOOK, code);
            model.put(RESPONSE_KEY_SUCCESS, true);
        } catch (WrongAccessCodeException e) {
            log.error(e,e);
            model.put(RESPONSE_KEY_MESSAGE,messageTemplateService.getMessage(MSG_SN_CONNECT_ERROR));
            model.put(RESPONSE_KEY_SUCCESS,false);
        } catch (DuplicatedAccountException e) {
            log.error(e,e);
            model.put(RESPONSE_KEY_MESSAGE,messageTemplateService.getMessage(MSG_SN_DUPLICATED_ACCOUNT));
            model.put(RESPONSE_KEY_SUCCESS,false);
        }
        return VIEW_REDIRECT;
    }
    
    @RequestMapping(value = "/vkontakte", method = RequestMethod.GET)
    public String linkVkontakte(ModelMap model, HttpSession session, @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "error", required = false) String error) {

        if (StringUtils.isNotBlank(error) || StringUtils.isBlank(code)) {
            model.put(RESPONSE_KEY_SUCCESS,false);
            model.put(RESPONSE_KEY_MESSAGE,messageTemplateService.getMessage(MSG_SN_CONNECT_ERROR));
            return VIEW_REDIRECT;
        }
        try {
            Long userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
            if (userId  == 0) {
                userId = (Long)session.getAttribute(PARAM_SESSION_USERID);
                if (userId == null) return VIEW_REDIRECT;
            }
            userService.attachAccount(userId, SocialNetworkType.VKONTAKTE, code);
            model.put(RESPONSE_KEY_SUCCESS, true);
        } catch (WrongAccessCodeException e) {
            log.error(e,e);
            model.put(RESPONSE_KEY_MESSAGE,messageTemplateService.getMessage(MSG_SN_CONNECT_ERROR));
            model.put(RESPONSE_KEY_SUCCESS,false);
        } catch (DuplicatedAccountException e) {
            log.error(e,e);
            model.put(RESPONSE_KEY_MESSAGE,messageTemplateService.getMessage(MSG_SN_DUPLICATED_ACCOUNT));
            model.put(RESPONSE_KEY_SUCCESS,false);
        }
        return VIEW_REDIRECT;
    }

    @RequestMapping(value = "/unlink", method = RequestMethod.POST)
    @ResponseBody
    public ModelMap unlinkSocialAccount(HttpSession session, @RequestParam(value = "account", required = true) String account) {

        ModelMap response = new ModelMap();
        try {
            SocialNetworkType snType = null ;
            if (account.equals("facebook")) {
                snType = SocialNetworkType.FACEBOOK;
            } else if (account.equals("vkontakte")) {
                snType = SocialNetworkType.VKONTAKTE;
            } else {
                return response;
            }
            Long userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
            if (userId  == 0) {
                userId = (Long)session.getAttribute(PARAM_SESSION_USERID);
                if (userId == null) return response;
            }
            userService.detachAccount(userId, snType);
            response.put(RESPONSE_KEY_SUCCESS, true);
        } catch (AccountNotExistsException e) {
            log.error(e,e);
        } catch (Exception e) {
            response.put(RESPONSE_KEY_SUCCESS, false);
            response.put(RESPONSE_KEY_MESSAGE, messageTemplateService.getMessage(MSG_SN_DISCONNECT_ERROR));
            log.error(e,e);
        }
        return response;
    }

    @RequestMapping(value = "/linked", method = RequestMethod.GET)
    @ResponseBody
    public ModelMap getLinkedAccount(HttpSession session) {

        ModelMap response = new ModelMap();
        try {
            Map<String, Boolean> data = new HashMap<String, Boolean>();
            Long userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
            if (userId == 0) {
                userId = (Long)session.getAttribute(PARAM_SESSION_USERID);
            }
            User user = userService.getUser(userId);
            if (user == null) {
                response.put(RESPONSE_KEY_SUCCESS, true);
                return response;
            }
            for (UserSocialAccount a : user.getAccounts()) {
                data.put(a.getType().toString(), true);
            }
            response.put("data",data);
            response.put(RESPONSE_KEY_SUCCESS, true);
        } catch (UserNotFoundException e) {
            response.put(RESPONSE_KEY_SUCCESS, false);
            response.put(RESPONSE_KEY_MESSAGE, "User not found.");
            log.error(e,e);
        }
        return response;
    }

}
