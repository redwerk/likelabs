package com.redwerk.likelabs.infrastructure.template;

import com.redwerk.likelabs.application.messaging.MessageTemplateService;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class MessageTemplateServiceImpl implements MessageTemplateService {

    @Autowired
    MessageSource messageSource;

    @Override
    public String getMessage(String messageName, String... parameters) {
        Locale currentLocale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(messageName, parameters, currentLocale);
    }
}
