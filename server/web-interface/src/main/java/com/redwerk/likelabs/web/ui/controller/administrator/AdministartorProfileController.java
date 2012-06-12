package com.redwerk.likelabs.web.ui.controller.administrator;

import org.springframework.stereotype.Controller;
import com.redwerk.likelabs.application.UserService;
import com.redwerk.likelabs.application.dto.user.UserData;
import com.redwerk.likelabs.application.messaging.exception.EmailMessagingException;
import com.redwerk.likelabs.domain.model.user.User;
import com.redwerk.likelabs.infrastructure.security.CustomUserDetails;
import com.redwerk.likelabs.web.ui.dto.UserDto;
import com.redwerk.likelabs.web.ui.validator.UserProfileValidator;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;

@PreAuthorize("hasRole('ROLE_SYSTEM_ADMIN')")
@Controller
@RequestMapping(value = "/administrator/profile")
public class AdministartorProfileController {

    private final UserProfileValidator validator = new UserProfileValidator();

    private static final String VIEW_PROFILE = "commons/general_profile";

    private final Logger log = LogManager.getLogger(getClass());

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public String initForm(ModelMap model) {

        UserDto user = new UserDto(userService.getUser(getUserId()));
        model.put("user", user);
        model.put("page", "profile");
        return VIEW_PROFILE;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String processSubmit(ModelMap model,
            @ModelAttribute("user") UserDto user, BindingResult result, SessionStatus status) {

        Long userId = getUserId();
        validator.validate(user, result);
        if (result.hasErrors()) {
            model.put("page", "profile");
            return VIEW_PROFILE;
        }
        User userOldData = userService.getUser(userId);
        String password = StringUtils.isBlank(user.getPassword()) ? userOldData.getPassword() : user.getPassword();
        try {
            userService.updateUser(userId, new UserData(user.getPhone(), password, user.getEmail(), userOldData.isPublishInSN(), userOldData.getEnabledEvents()));
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

    private Long getUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof CustomUserDetails) {
            return ((CustomUserDetails)principal).getId();
        }
        return null;
    }
}