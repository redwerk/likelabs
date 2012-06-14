package com.redwerk.likelabs.web.ui.validator;

import com.redwerk.likelabs.application.template.MessageTemplateService;
import com.redwerk.likelabs.web.ui.dto.PointDto;
import com.redwerk.likelabs.web.ui.dto.TabletDto;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

public class PointProfileValidator implements org.springframework.validation.Validator {

    private static final Byte LENGTH_PHONE = 20;
    private static final Byte LENGTH_EMAIL = 40;
    private static final Byte LENGTH_POSTAL_CODE = 40;
    private static final Byte LENGTH_ADDRESS_FIELD = 80;
    private final static Byte MAX_LENGTH_FIELD_TABLET = 20;
    private final Validator mailValidator = new EmailValidator();
    private final Validator phoneValidator = new PhoneValidator();

    @Override
    public boolean supports(Class<?> clazz) {
        return PointDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        PointDto point = (PointDto) target;

        if (!mailValidator.isValid(point.getEmail())) {
            errors.rejectValue("email", "point.profile.invalid.email", "Please enter valid field.");
        }
        if (!phoneValidator.isValid(point.getPhone())) {
            errors.rejectValue("phone", "point.profile.invalid.phone", "Please enter valid field.");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "city",
                "point.profile.invalid.field.required", "Please fill in the required field.");

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "state",
                "point.profile.invalid.field.required", "Please fill in the required field.");

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "country",
                "point.profile.invalid.field.required", "Please fill in the required field.");

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "addressLine1",
                "point.profile.invalid.field.required", "Please fill in the required field.");

        if (point.getPhone() != null && point.getPhone().length() > LENGTH_PHONE) {
            errors.rejectValue("phone", "point.profile.invalid.phone.length", new Byte[]{LENGTH_PHONE} ,"Maximum length allowed is " + LENGTH_PHONE +" symbols.");
        }
        if (point.getEmail() != null && point.getEmail().length() > LENGTH_EMAIL) {
            errors.rejectValue("email", "point.profile.invalid.email.length",new Byte[]{LENGTH_EMAIL},"Maximum length allowed is " + LENGTH_EMAIL + " symbols.");
        }
        if (point.getPostalCode() != null && point.getPostalCode().length() > LENGTH_POSTAL_CODE) {
            errors.rejectValue("postalCode", "point.profile.invalid.postal.length", new Byte[]{LENGTH_POSTAL_CODE},"Maximum length allowed is " + LENGTH_POSTAL_CODE + " symbols.");
        }
        if (point.getCity() != null && point.getCity().length() > LENGTH_ADDRESS_FIELD) {
            errors.rejectValue("city", "point.profile.invalid.city.length", new Byte[]{LENGTH_ADDRESS_FIELD},"Maximum length allowed is " + LENGTH_ADDRESS_FIELD + " symbols.");
        }
        if (point.getState() != null && point.getState().length() > LENGTH_ADDRESS_FIELD) {
            errors.rejectValue("state", "point.profile.invalid.state.length", new Byte[]{LENGTH_ADDRESS_FIELD},"Maximum length allowed is " + LENGTH_ADDRESS_FIELD + " symbols.");
        }
        if (point.getCountry() != null && point.getCountry().length() > LENGTH_ADDRESS_FIELD) {
            errors.rejectValue("country", "point.profile.invalid.country.length", new Byte[]{LENGTH_ADDRESS_FIELD},"Maximum length allowed is " + LENGTH_ADDRESS_FIELD + " symbols.");
        }
        if (point.getAddressLine1() != null && point.getAddressLine1().length() > LENGTH_ADDRESS_FIELD) {
            errors.rejectValue("addressLine1", "point.profile.invalid.addressline.length", new Byte[]{LENGTH_ADDRESS_FIELD},"Maximum length allowed is " + LENGTH_ADDRESS_FIELD + " symbols.");
        }
        if (point.getAddressLine2() != null && point.getAddressLine2().length() > LENGTH_ADDRESS_FIELD) {
            errors.rejectValue("addressLine2", "point.profile.invalid.addressline.length", new Byte[]{LENGTH_ADDRESS_FIELD},"Maximum length allowed is "  + LENGTH_ADDRESS_FIELD + " symbols.");
        }
    }

    public List<String> validateTablet(TabletDto tablet, MessageTemplateService messageTemplateService) {

        List<String> errors = new ArrayList<String>();
        if (StringUtils.isBlank(tablet.getLogin())) {
            errors.add(messageTemplateService.getMessage("point.profile.empty.login"));
        }
        if (StringUtils.isBlank(tablet.getLoginPassword())) {
            errors.add(messageTemplateService.getMessage("point.profile.empty.loginpass"));
        }
        if (StringUtils.isBlank(tablet.getLogoutPassword())) {
            errors.add(messageTemplateService.getMessage("point.profile.empty.logoutpass"));
        }
        if (tablet.getLogin().length() > MAX_LENGTH_FIELD_TABLET) {
            errors.add(messageTemplateService.getMessage("point.profile.invalid.login.length", MAX_LENGTH_FIELD_TABLET.toString()));
        }
        if (tablet.getLoginPassword().length() > MAX_LENGTH_FIELD_TABLET) {
            errors.add(messageTemplateService.getMessage("point.profile.invalid.loginpass.length", MAX_LENGTH_FIELD_TABLET.toString()));
        }
        if (tablet.getLogoutPassword().length() > MAX_LENGTH_FIELD_TABLET) {
            errors.add(messageTemplateService.getMessage("point.profile.invalid.logoutpass.length", MAX_LENGTH_FIELD_TABLET.toString()));
        }
        return errors;
    }
}
