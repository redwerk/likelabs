package com.redwerk.likelabs.application.notification;

import com.redwerk.likelabs.application.messaging.EmailService;
import com.redwerk.likelabs.application.messaging.SmsService;
import com.redwerk.likelabs.application.messaging.exception.SmsMessagingException;
import com.redwerk.likelabs.application.template.MessageTemplateService;
import com.redwerk.likelabs.domain.model.company.Company;
import com.redwerk.likelabs.domain.model.review.Review;
import com.redwerk.likelabs.domain.model.user.User;
import com.redwerk.likelabs.domain.service.RecipientNotifier;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class RecipientNotifierImpl implements RecipientNotifier {
    
    private static final String REVIEW_URL_TEMPLATE = "{0}/public/review/{1}";

    private static final String APPLICATION_DOMAIN =  "app.domain";

    private static final String NOTIFICATION_TEXT = "review.creation.notification.text";

    private static final String NOTIFICATION_SUBJECT = "review.creation.notification.subject";

    private static final String NOTIFICATION_SENDER = "review.creation.notification.sender";

    private static final int MAX_TEXT_LENGTH = 50;


    private static final Logger LOGGER = Logger.getLogger(RecipientNotifierImpl.class);

    @Autowired
    private EmailService emailService;

    @Autowired
    private SmsService smsService;

    @Autowired
    private MessageTemplateService messageService;


    @Override
    public boolean notifyBySms(String phone, Review review) {
        try {
            smsService.sendMessage(phone, getMessageText(review, true));
        }
        catch (SmsMessagingException e) {
            LOGGER.error("cannot notify review recipient by SMS " + phone, e);
            return false;
        }
        return true;
    }

    @Override
    public boolean notifyByEmail(String email, Review review) {
        try {
            emailService.sendMessage(email, messageService.getMessage(NOTIFICATION_SENDER),
                    messageService.getMessage(NOTIFICATION_SUBJECT), getMessageText(review, false));
        }
        catch (SmsMessagingException e) {
            LOGGER.error("cannot notify review recipient by email " + email, e);
            return false;
        }
        return true;
    }
    
    private String getMessageText(Review review, boolean truncate) {
        User author = review.getAuthor();
        String authorName = author.isAnonymous() ?
                (author.getName() + " (" + author.getPhone() + ")" ) :
                author.getName();
        String text = review.getMessage();
        if (truncate && text.length() > MAX_TEXT_LENGTH) {
            text = text.substring(0, MAX_TEXT_LENGTH) + "...";
        }
        return messageService.getMessage(
                NOTIFICATION_TEXT, authorName, review.getCompany().getName(), text, getReviewUrl(review));
    }
    
    private String getReviewUrl(Review review) {
        return MessageFormat.format(REVIEW_URL_TEMPLATE, messageService.getMessage(APPLICATION_DOMAIN), review.getId());
    }

}
