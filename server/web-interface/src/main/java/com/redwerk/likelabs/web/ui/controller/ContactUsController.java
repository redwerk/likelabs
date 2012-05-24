package com.redwerk.likelabs.web.ui.controller;

import com.redwerk.likelabs.application.messaging.EmailService;
import com.redwerk.likelabs.application.messaging.MessageTemplateService;
import com.redwerk.likelabs.application.messaging.exception.EmailMessagingException;
import com.redwerk.likelabs.web.ui.controller.dto.ContactUsMessageDto;
import com.redwerk.likelabs.web.ui.validator.EmailValidator;
import com.redwerk.likelabs.web.ui.validator.Validator;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;

@Controller
@RequestMapping(value = "/contact")
public class ContactUsController {

    private ContactUsMailValidator validator = new ContactUsMailValidator();
    private final Logger log = LogManager.getLogger(getClass());
    @Autowired
    private EmailService emailService;
    @Autowired
    private MessageTemplateService messageTemplateService;

    @RequestMapping(method = RequestMethod.GET)
    public String initForm(ModelMap model) {

        ContactUsMessageDto message = new ContactUsMessageDto();
        model.addAttribute("message", message);
        model.addAttribute("page", "contact");
        return "contact_us";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String processSubmit(ModelMap model, @ModelAttribute("message") ContactUsMessageDto message,
            BindingResult result, SessionStatus status) {
        model.addAttribute("page", "contact");
        validator.validate(message, result);
        if (result.hasErrors()) {
            return "contact_us";
        }
        try {
            emailService.sendMessage(messageTemplateService.getMessage("app.email.contact.us"),
                    message.getEmail(), message.getName() + ": " + message.getSummary(), message.getMessage());
        } catch (EmailMessagingException e) {
            log.error(e, e);
            model.addAttribute("error", true);
        }
        status.setComplete();
        return "contact_us_success";
    }

    private class ContactUsMailValidator implements org.springframework.validation.Validator {

        private static final int DEFAULT_LENGTH = 80;
        private final Validator mailValidator = new EmailValidator();

        @Override
        public boolean supports(Class<?> clazz) {
            return ContactUsMessageDto.class.isAssignableFrom(clazz);
        }

        @Override
        public void validate(Object target, Errors errors) {

            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "message",
                    "contactus.form.required.field", "Please fill in the required fields.");

            ContactUsMessageDto mail = (ContactUsMessageDto) target;
            
            if (StringUtils.isBlank(mail.getEmail())) {
                errors.rejectValue("email", "contactus.form.required.field", "Please fill in the required fields.");
            } else if (!mailValidator.isValid(mail.getEmail())) {
                errors.rejectValue("email", "contactus.form.ivalide.email", "Please enter valid email address.");
            }
            if (mail.getName().length() > DEFAULT_LENGTH) {
                errors.rejectValue("name", "contactus.form.ivalide.long.name", "Maximum name length allowed is 40 symbols.");
            }

            if (mail.getSummary().length() > DEFAULT_LENGTH) {
                errors.rejectValue("summary", "contactus.form.ivalide.long.summary", "Maximum summary length allowed is 80 symbols.");
            }
        }
    }
}
