package com.redwerk.likelabs.application.notification;

import com.redwerk.likelabs.application.messaging.EmailService;
import com.redwerk.likelabs.application.messaging.SmsService;
import com.redwerk.likelabs.application.messaging.exception.SmsMessagingException;
import com.redwerk.likelabs.domain.service.RecipientNotifier;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RecipientNotifierImpl implements RecipientNotifier {

    private static final Logger LOGGER = Logger.getLogger(RecipientNotifierImpl.class);


    @Autowired
    private EmailService emailService;

    @Autowired
    private SmsService smsService;


    @Override
    public boolean notifyBySms(String phone, long reviewId, String authorName) {
        try {
            smsService.sendMessage(phone, "New review was created by " + authorName);
        }
        catch (SmsMessagingException e) {
            LOGGER.error("cannot notify review recipient by SMS " + phone, e);
            return false;
        }
        return true;
    }

    @Override
    public boolean notifyByEmail(String email, long reviewId, String authorName) {
        try {
            emailService.sendMessage(email, "likelabs", "new review", "New review was created by " + authorName);
        }
        catch (SmsMessagingException e) {
            LOGGER.error("cannot notify review recipient by email " + email, e);
            return false;
        }
        return true;
    }

}
