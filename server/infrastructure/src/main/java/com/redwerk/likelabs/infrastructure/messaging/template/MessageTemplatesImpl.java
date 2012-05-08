package com.redwerk.likelabs.infrastructure.messaging.template;

import com.redwerk.likelabs.application.messaging.MessageTemplates;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class MessageTemplatesImpl implements MessageTemplates {

    @Autowired
    MessageSource messageSource;

    @Override
    public String getMessage(String messageName, String... parameters) {
        Locale currentLocale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(messageName, parameters, currentLocale);
    }
}
