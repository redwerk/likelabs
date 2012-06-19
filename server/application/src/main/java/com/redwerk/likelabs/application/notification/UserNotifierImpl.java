package com.redwerk.likelabs.application.notification;

import com.redwerk.likelabs.application.messaging.EmailService;
import com.redwerk.likelabs.application.messaging.SmsService;
import com.redwerk.likelabs.application.messaging.exception.EmailMessagingException;
import com.redwerk.likelabs.application.messaging.exception.SmsMessagingException;
import com.redwerk.likelabs.application.template.MessageTemplateService;
import com.redwerk.likelabs.domain.model.event.Event;
import com.redwerk.likelabs.domain.model.event.EventType;
import com.redwerk.likelabs.domain.model.notification.WarningType;
import com.redwerk.likelabs.domain.model.user.User;
import com.redwerk.likelabs.domain.service.UserNotifier;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserNotifierImpl implements UserNotifier {

    private static final Logger LOGGER = Logger.getLogger(RecipientNotifierImpl.class);

    private static final String USER_REVIEW_URL_TEMPLATE = "{0}/user/{1}/review/{2}";

    private static final String PUBLIC_REVIEW_URL_TEMPLATE = "{0}/public/review/{1}";

    private static final String APPLICATION_DOMAIN =  "app.domain";

    private static final String NOTIFICATION_SUBJECT = "user.notification.subject";

    private static final String NOTIFICATION_SENDER = "user.notification.sender";

    private static final Map<EventType, String> eventMessages = new HashMap<EventType, String>() {{
        put(EventType.USER_REVIEW_CREATED, "user.notification.new_review");
        put(EventType.CLIENT_REVIEW_CREATED, "user.notification.new_client_review");
        put(EventType.USER_REVIEW_APPROVED, "user.notification.review_approved");
    }};

    private static final Map<WarningType, String> warningMessages = new HashMap<WarningType, String>() {{
        put(WarningType.EMAIL_IS_ABSENT, "user.notification.no_email");
    }};

    @Autowired
    private EmailService emailService;

    @Autowired
    private SmsService smsService;

    @Autowired
    private MessageTemplateService messageService;


    @Override
    public boolean sendEmail(User user, EventType eventType, List<Event> events) {
        try {
            emailService.sendMessage(user.getEmail(), messageService.getMessage(NOTIFICATION_SENDER),
                    messageService.getMessage(NOTIFICATION_SUBJECT), getNotificationText(user, eventType, events));
        }
        catch (EmailMessagingException e) {
            LOGGER.error("cannot notify user by email " + user.getEmail(), e);
            return false;
        }
        LOGGER.info("user was notified successfully " + user.getEmail());
        return true;
    }

    @Override
    public boolean sendSms(User user,  EventType eventType, List<Event> events) {
        try {
            smsService.sendMessage(user.getPhone(), getNotificationText(user, eventType, events));
        }
        catch (SmsMessagingException e) {
            LOGGER.error("cannot notify user by SMS " + user.getPhone(), e);
            return false;
        }
        LOGGER.info("user was notified successfully " + user.getPhone());
        return true;
    }

    @Override
    public boolean sendWarning(User user, WarningType warningType) {
        try {
            String messageTemplate = warningMessages.get(warningType);
            smsService.sendMessage(user.getPhone(), messageService.getMessage(messageTemplate));
        }
        catch (SmsMessagingException e) {
            LOGGER.error("cannot send SMS with warning to " + user.getPhone(), e);
            return false;
        }
        LOGGER.info("user was notified successfully " + user.getPhone());
        return true;
    }
    
    private String getNotificationText(User user, EventType eventType, List<Event> events) {
        String messageTemplate = eventMessages.get(eventType);
        String lineTemplate = (eventType == EventType.CLIENT_REVIEW_CREATED) ? getPublicLinkTemplate() : getUserLinkTemplate(user);
        StringBuilder text = new StringBuilder(messageService.getMessage(messageTemplate))
                .append(":").append("\n");
        boolean isFirstLine = true;
        for (Event e: events) {
            if (isFirstLine) {
                isFirstLine = false;
            }
            else {
                text.append(",");
            }
            text.append("\n").append(getEventLine(e, lineTemplate));
        }
        return text.toString();
    }
    
    private String getEventLine(Event event, String lineTemplate) {
        return MessageFormat.format(lineTemplate, event.getReview().getId());
    }

    private String getUserLinkTemplate(User user) {
        return MessageFormat.format(USER_REVIEW_URL_TEMPLATE, messageService.getMessage(APPLICATION_DOMAIN), user.getId(), "{0}");
    }

    private String getPublicLinkTemplate() {
        return MessageFormat.format(PUBLIC_REVIEW_URL_TEMPLATE, messageService.getMessage(APPLICATION_DOMAIN), "{0}");
    }


}
