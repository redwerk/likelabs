package com.redwerk.likelabs.domain.service.notification;

import com.redwerk.likelabs.domain.model.event.Event;
import com.redwerk.likelabs.domain.model.event.EventRepository;
import com.redwerk.likelabs.domain.model.notification.NotificationInterval;
import com.redwerk.likelabs.domain.model.notification.NotificationIntervalRepository;
import com.redwerk.likelabs.domain.model.notification.Period;
import com.redwerk.likelabs.domain.model.notification.WarningType;
import com.redwerk.likelabs.domain.model.query.Pager;
import com.redwerk.likelabs.domain.model.user.User;
import com.redwerk.likelabs.domain.model.user.UserRepository;
import com.redwerk.likelabs.domain.service.UserNotifier;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class NotificationProcessor {

    private static final Logger LOG = Logger.getLogger(NotificationProcessor.class);

    private final UserRepository userRepository;

    private final EventRepository eventRepository;

    private final NotificationIntervalRepository intervalRepository;

    private final UserNotifier userNotifier;


    public NotificationProcessor(UserRepository userRepository, EventRepository eventRepository,
                                 NotificationIntervalRepository intervalRepository,
                                 UserNotifier userNotifier) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.intervalRepository = intervalRepository;
        this.userNotifier = userNotifier;
    }

    public void processEvent(Event event) {
        Validate.notNull(event);
        NotificationInterval interval = intervalRepository.get(event.getType());
        if (interval.getEmailInterval() == Period.IMMEDIATELY) {
            if (userNotifier.sendEmail(event.getUser(), Collections.singletonList(event))) {
                event.markAsNotified();
            }
        }
        if (interval.getSmsInterval() == Period.IMMEDIATELY) {
            if (userNotifier.sendSms(event.getUser(), Collections.singletonList(event))) {
                event.markAsNotified();
            }
        }
    }

    public void processPendingEvents() {
        List<User> users = userRepository.findRegular(Pager.ALL_RECORDS);
        Calendar calendar = Calendar.getInstance();
        boolean  monthRunning = (calendar.get(Calendar.DAY_OF_MONTH) == 1);
        boolean  weekRunning = (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY);
        for (NotificationInterval interval: intervalRepository.findAll()) {
            boolean emailQualified = (isQualified(interval.getEmailInterval(), monthRunning, weekRunning));
            boolean smsQualified = (isQualified(interval.getSmsInterval(), monthRunning, weekRunning));
            if (emailQualified || smsQualified) {
                for (User u: users) {
                    if (interval.getWarningType() != null) {
                        if (interval.getWarningType() != WarningType.EMAIL_IS_ABSENT) {
                            LOG.error("Unknown warning type: " + interval.getWarningType());
                        }
                        else if (StringUtils.isEmpty(u.getEmail())) {
                            userNotifier.sendWarning(u);
                        }
                        continue;
                    }
                    List<Event> events = eventRepository.findPending(u, interval.getEventType());
                    if (!events.isEmpty()) {
                        if (emailQualified) {
                            if (userNotifier.sendEmail(u, events)) {
                                markAsNotified(events);
                                LOG.error("Email notification was sent to user: " + u);
                            }
                            else {
                                LOG.error("Email notification sending error for user: " + u);
                            }
                        }
                        if (smsQualified) {
                            if (userNotifier.sendSms(u, events)) {
                                markAsNotified(events);
                                LOG.error("Sms notification was sent to user: " + u);
                            }
                            else {
                                LOG.error("Sms notification sending error for user: " + u);
                            }
                        }
                    }
                    
                }
            }
        }
    }

    private boolean isQualified(Period period, boolean monthRunning, boolean  weekRunning) {
        return (period == Period.DAILY) || (monthRunning && period == Period.MONTHLY) || (weekRunning && period == Period.WEEKLY);
    }
    
    private void markAsNotified(List<Event> events) {
        for (Event e: events) {
            e.markAsNotified();
        }
    }

}
