package com.redwerk.likelabs.web.ui.validator;

import com.redwerk.likelabs.application.dto.company.CompanyAdminData;
import com.redwerk.likelabs.application.template.MessageTemplateService;
import com.redwerk.likelabs.web.ui.dto.CompanyDto;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;


public class CompanyProfileValidator implements org.springframework.validation.Validator {

    private static final Byte MAX_LENGTH_NAME = 80;
    private static final Byte MAX_LENGTH_PHONE = 20;
    private static final Byte MAX_LENGTH_EMAIL = 40;
    private final Validator mailValidator = new EmailValidator();
    private final Validator phoneValidator = new PhoneValidator();

    @Override
    public boolean supports(Class<?> clazz) {
        return CompanyDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name",
                "company.profile.invalid.name.required", "Please fill in the required field.");

        CompanyDto company = (CompanyDto) target;

        if (!mailValidator.isValid(company.getEmail())) {
            errors.rejectValue("email", "company.profile.invalid.email", "Please enter valid field.");
        }
        if (!phoneValidator.isValid(company.getPhone())) {
            errors.rejectValue("phone", "company.profile.invalid.phone", "Please enter valid field.");
        }
        if (company.getPhone() != null && company.getPhone().length() > MAX_LENGTH_PHONE) {
            errors.rejectValue("phone", "company.profile.invalid.phone.length", new Byte[]{MAX_LENGTH_PHONE} ,"Maximum length allowed is "+MAX_LENGTH_PHONE+" symbols.");
        }
        if (company.getName() != null && company.getName().length() > MAX_LENGTH_NAME) {
            errors.rejectValue("name", "company.profile.invalid.name.length", new Byte[]{MAX_LENGTH_NAME} ,"Maximum length allowed is "+MAX_LENGTH_NAME+" symbols.");
        }
        if (company.getEmail() != null && company.getEmail().length() > MAX_LENGTH_EMAIL) {
            errors.rejectValue("email", "company.profile.invalid.email.length", new Byte[]{MAX_LENGTH_EMAIL} ,"Maximum length allowed is "+MAX_LENGTH_EMAIL+" symbols.");
        }
    }

    public List<String> validateOnCreate(CompanyDto company, CompanyAdminData admin,MessageTemplateService messageTemplateService) {

        List<String> errors = new ArrayList<String>();
        if (StringUtils.isBlank(company.getName())) {
            errors.add(messageTemplateService.getMessage("company.profile.invalid.name.required"));
        }
        if (!mailValidator.isValid(admin.getEmail())) {
            errors.add(messageTemplateService.getMessage("user.profile.invalid.email"));
        }
        if (!phoneValidator.isValid(admin.getPhone())) {
            errors.add(messageTemplateService.getMessage("user.profile.invalid.phone"));
        }
        if (admin.getEmail().length() > MAX_LENGTH_EMAIL) {
            errors.add(messageTemplateService.getMessage("user.profile.invalid.length.email", MAX_LENGTH_EMAIL.toString()));
        }
        if (admin.getPhone().length() > MAX_LENGTH_PHONE) {
            errors.add(messageTemplateService.getMessage("user.profile.invalid.length.phone", MAX_LENGTH_PHONE.toString()));
        }


        if (!mailValidator.isValid(company.getEmail())) {
            errors.add(messageTemplateService.getMessage("company.profile.invalid.email"));
        }
        if (!phoneValidator.isValid(company.getPhone())) {
            errors.add(messageTemplateService.getMessage("company.profile.invalid.phone"));
        }
        if (company.getEmail().length() > MAX_LENGTH_EMAIL) {
            errors.add(messageTemplateService.getMessage("company.profile.invalid.email.length", MAX_LENGTH_EMAIL.toString()));
        }
        if (company.getPhone().length() > MAX_LENGTH_PHONE) {
            errors.add(messageTemplateService.getMessage("company.profile.invalid.phone.length", MAX_LENGTH_PHONE.toString()));
        }
        if (company.getName().length() > MAX_LENGTH_NAME) {
            errors.add(messageTemplateService.getMessage("company.profile.invalid.name.length", MAX_LENGTH_NAME.toString()));
        }
        return errors;
    }
}

