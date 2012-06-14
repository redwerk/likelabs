package com.redwerk.likelabs.web.ui.validator;

import com.redwerk.likelabs.application.template.MessageTemplateService;
import com.redwerk.likelabs.web.ui.dto.UserDto;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;

public class UserProfileValidator implements org.springframework.validation.Validator {

    private static final Byte MAX_LENGTH_PHONE = 20;
    private static final Byte MAX_LENGTH_EMAIL = 40;
    private static final Byte MAX_LENGTH_PASSWORD = 40;
    private static final byte NEW_USER_ID = 0;
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
            errors.rejectValue("email", "user.profile.invalid.email", "Please enter valid email address.");
        }
        if (!phoneValidator.isValid(user.getPhone())) {
            errors.rejectValue("phone", "user.profile.invalid.phone", "Please enter valid phone number.");
        }
        if (StringUtils.isNotBlank(user.getPassword())) {
            if (!user.getPassword().equals(user.getConfirmPassword())) {
                errors.rejectValue("confirmPassword", "user.profile.invalid.confirmpassword", "The passwords do not match.");
            }
        }
        if (StringUtils.isNotBlank(user.getPhone()) && user.getPhone().length() > MAX_LENGTH_PHONE) {
            errors.rejectValue("phone", "user.profile.invalid.length.phone", new Byte[]{MAX_LENGTH_PHONE},
                    "Maximum phone length allowed is " + MAX_LENGTH_PHONE + " symbols.");
        }
        if (StringUtils.isNotBlank(user.getEmail()) && user.getEmail().length() > MAX_LENGTH_EMAIL) {
            errors.rejectValue("email", "user.profile.invalid.length.email", new Byte[]{MAX_LENGTH_EMAIL},
                    "Maximum E-mail length allowed is " + MAX_LENGTH_EMAIL + " symbols.");
        }
        if (StringUtils.isNotBlank(user.getPassword()) && user.getPassword().length() > MAX_LENGTH_PASSWORD) {
            errors.rejectValue("password", "user.profile.invalid.length.passwordh", new Byte[]{MAX_LENGTH_PASSWORD},
                    "Maximum password length allowed is " + MAX_LENGTH_PASSWORD + " symbols.");
        }
    }

    public List<String> validate(UserDto user, MessageTemplateService messageTemplateService) {

        List<String> errors = validateWithoutPassword(user, messageTemplateService);
        if (StringUtils.isBlank(user.getPassword())) {
            errors.add(messageTemplateService.getMessage("user.profile.invalid.password"));
            return errors;
        }
        if (user.getPassword().length() > MAX_LENGTH_PASSWORD) {
            errors.add(messageTemplateService.getMessage("user.profile.invalid.length.password", MAX_LENGTH_PASSWORD.toString()));
        }
        return errors;
    }

    public List<String> validateWithoutPassword(UserDto user, MessageTemplateService messageTemplateService) {

        List<String> errors = new ArrayList<String>();
        if (!mailValidator.isValid(user.getEmail())) {
            errors.add(messageTemplateService.getMessage("user.profile.invalid.email"));
        }
        if (!phoneValidator.isValid(user.getPhone())) {
            errors.add(messageTemplateService.getMessage("user.profile.invalid.phone"));
        }
        if (StringUtils.isNotBlank(user.getEmail()) && user.getEmail().length() > MAX_LENGTH_EMAIL) {
            errors.add(messageTemplateService.getMessage("user.profile.invalid.length.email", MAX_LENGTH_EMAIL.toString()));
        }
        if (StringUtils.isNotBlank(user.getPhone()) && user.getPhone().length() > MAX_LENGTH_PHONE) {
            errors.add(messageTemplateService.getMessage("user.profile.invalid.length.phone", MAX_LENGTH_PHONE.toString()));
        }
        return errors;
    }
}
