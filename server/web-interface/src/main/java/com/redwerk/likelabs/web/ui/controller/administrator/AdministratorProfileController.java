package com.redwerk.likelabs.web.ui.controller.administrator;

import org.springframework.stereotype.Controller;
import com.redwerk.likelabs.application.UserService;
import com.redwerk.likelabs.application.dto.user.UserProfileData;
import com.redwerk.likelabs.application.messaging.exception.EmailMessagingException;
import com.redwerk.likelabs.web.ui.dto.UserDto;
import com.redwerk.likelabs.web.ui.security.Authenticator;
import com.redwerk.likelabs.web.ui.validator.UserProfileValidator;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;

@PreAuthorize("hasRole('ROLE_SYSTEM_ADMIN')")
@Controller
@RequestMapping(value = "/administrator/profile")
public class AdministratorProfileController {

    private final UserProfileValidator validator = new UserProfileValidator();

    private static final String VIEW_PROFILE = "commons/general_profile";

    private final Logger log = LogManager.getLogger(getClass());

    @Autowired
    private UserService userService;

    @Autowired
    private Authenticator authenticator;

    @RequestMapping(method = RequestMethod.GET)
    public String initForm(ModelMap model) {

        UserDto user = new UserDto(userService.getUser(authenticator.getCurrentUserId()));
        model.put("user", user);
        model.put("page", "profile");
        return VIEW_PROFILE;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String processSubmit(ModelMap model,
            @ModelAttribute("user") UserDto user, BindingResult result, SessionStatus status) {

        Long userId = authenticator.getCurrentUserId();
        validator.validate(user, result);
        if (result.hasErrors()) {
            model.put("page", "profile");
            return VIEW_PROFILE;
        }
        String password = StringUtils.isBlank(user.getPassword()) ? null : user.getPassword();
        try {
            userService.updateProfile(userId, new UserProfileData(user.getPhone(), password, user.getEmail()));
        } catch (EmailMessagingException e) {
            log.error(e,e);
            result.rejectValue("email", "user.profile.invalid.email", "Please enter valid email address.");
            model.put("page", "profile");
            return VIEW_PROFILE;
        }
        status.setComplete();
        model.clear();
        return "redirect:/administrator";
    }
}
