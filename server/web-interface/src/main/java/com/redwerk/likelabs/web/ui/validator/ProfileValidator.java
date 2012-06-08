package com.redwerk.likelabs.web.ui.validator;

import com.redwerk.likelabs.web.ui.dto.ProfileData;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class ProfileValidator implements Validator {

    @Override
    public boolean supports(Class type) {
        return ProfileData.class.isAssignableFrom(type);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ProfileData profileData = (ProfileData) target;
        if (!(new PhoneValidator().isValid(profileData.getPhone()))) {
            errors.rejectValue("phone", "phoneField.notValid");
        }
        if (!(new EmailValidator().isValid(profileData.getEmail()))) {
            errors.rejectValue("email", "emailField.notValid");
        }
    }
    
}
