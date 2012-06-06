package com.redwerk.likelabs.web.ui.controller.user;

import com.redwerk.likelabs.application.UserService;
import com.redwerk.likelabs.application.dto.user.UserData;
import com.redwerk.likelabs.application.template.MessageTemplateService;
import com.redwerk.likelabs.domain.model.user.User;
import com.redwerk.likelabs.web.ui.controller.dto.UserDto;
import com.redwerk.likelabs.web.ui.validator.EmailValidator;
import com.redwerk.likelabs.web.ui.validator.PhoneValidator;
import com.redwerk.likelabs.web.ui.validator.Validator;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;

@Controller
@RequestMapping(value = "/user/{userId}/profile")
public class UserProfileController {

    private static final String VIEW_USER_PROFILE = "user/profile";
    private final UserProfileValidator validator = new UserProfileValidator();

    private final Logger log = LogManager.getLogger(getClass());

    @Autowired
    private MessageTemplateService messageTemplateService;

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public String initForm(ModelMap model, @PathVariable Integer userId) {

        UserDto user = new UserDto(userService.getUser(userId));
        model.put("user", user);
        model.put("page", "profile");
        model.put("cabinet", "user");
        return VIEW_USER_PROFILE;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String processSubmit(ModelMap model, @PathVariable Integer userId,
            @ModelAttribute("user") UserDto user, BindingResult result, SessionStatus status) {

        validator.validate(user, result);
        if (result.hasErrors()) {
            model.put("page", "profile");
            model.put("cabinet", "user");
            return VIEW_USER_PROFILE;
        }
        User userOldData = userService.getUser(userId);
        String password = StringUtils.isBlank(user.getPassword()) ? userOldData.getPassword() : user.getPassword();
        userService.updateUser(userId, new UserData(user.getPhone(), password, user.getEmail(), userOldData.isPublishInSN(), userOldData.getEnabledEvents()));
        status.setComplete();
        model.clear();
        return "redirect:/user/" + userId;
    }

    private class UserProfileValidator implements org.springframework.validation.Validator {

        private final Byte MAX_LENGTH_PHONE = 20;
        private final Byte MAX_LENGTH_EMAIL = 40;
        private final Byte MAX_LENGTH_PASSWORD = 40;
        private final Validator mailValidator = new EmailValidator();
        private final Validator phoneValidator = new PhoneValidator();

        @Override
        public boolean supports(Class<?> clazz) {
            return UserDto.class.isAssignableFrom(clazz);
        }

        @Override
        public void validate(Object target, Errors errors) {

            UserDto user = (UserDto)target;

            if (!mailValidator.isValid(user.getEmail())) {
                errors.rejectValue("email", "user.profile.invalid.email", "Please enter valid e-mail address.");
            }
            if (!phoneValidator.isValid(user.getPhone())) {
                errors.rejectValue("phone", "user.profile.invalid.phone", "Please enter valid phone number.");
            }
            if (StringUtils.isNotBlank(user.getPassword())) {
                if (!user.getPassword().equals(user.getConfirmPassword())) {
                    errors.rejectValue("confirmPassword", "user.profile.invalid.confirmpassword", "The passwords do not match.");
                }
            }
            if (user.getPhone() != null && user.getPhone().length() > MAX_LENGTH_PHONE) {
                errors.rejectValue("phone", "user.profile.invalid.length.phone", new Byte[]{MAX_LENGTH_PHONE} ,
                         "Maximum phone length allowed is " + MAX_LENGTH_PHONE +" symbols.");
            }
            if (user.getEmail() != null && user.getEmail().length() > MAX_LENGTH_EMAIL) {
                errors.rejectValue("email", "user.profile.invalid.length.email",new Byte[]{MAX_LENGTH_EMAIL},
                         "Maximum E-mail length allowed is " + MAX_LENGTH_EMAIL + " symbols.");
            }
            if (user.getEmail() != null && user.getEmail().length() > MAX_LENGTH_PASSWORD) {
                errors.rejectValue("password", "user.profile.invalid.length.passwordh",new Byte[]{MAX_LENGTH_PASSWORD},
                         "Maximum password length allowed is " + MAX_LENGTH_PASSWORD + " symbols.");
            }
        }
    }
}
