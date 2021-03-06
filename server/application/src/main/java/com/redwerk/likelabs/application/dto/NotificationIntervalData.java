package com.redwerk.likelabs.application.dto;

import com.redwerk.likelabs.domain.model.event.EventType;
import com.redwerk.likelabs.domain.model.notification.Period;
import com.redwerk.likelabs.domain.model.notification.WarningType;
import org.apache.commons.lang.Validate;

public class NotificationIntervalData {

    private final EventType eventType;

    private final WarningType warningType;

    private final Period emailInterval;

    private final Period smsInterval;

    public NotificationIntervalData(EventType eventType, WarningType warningType,
                                    Period emailInterval, Period smsInterval) {
        Validate.isTrue((eventType != null && warningType == null) || (eventType == null && warningType != null));
        this.eventType = eventType;
        this.warningType = warningType;
        this.emailInterval = emailInterval;
        this.smsInterval = smsInterval;
    }

    public EventType getEventType() {
        return eventType;
    }

    public WarningType getWarningType() {
        return warningType;
    }

    public Period getEmailInterval() {
        return emailInterval;
    }

    public Period getSmsInterval() {
        return smsInterval;
    }
}
