package com.redwerk.likelabs.web.ui.dto;

import com.redwerk.likelabs.application.dto.NotificationIntervalData;
import com.redwerk.likelabs.domain.model.event.EventType;
import com.redwerk.likelabs.domain.model.notification.NotificationInterval;
import com.redwerk.likelabs.domain.model.notification.Period;
import com.redwerk.likelabs.domain.model.notification.WarningType;
import java.util.ArrayList;
import java.util.List;

@Deprecated
public class NotificationIntervalDto {


    private EventType eventType;
    private WarningType warningType;
    private String description;
    private Period emailInterval;
    private Period smsInterval;

    public NotificationIntervalDto() {
    }

    public NotificationIntervalDto(EventType eventType, WarningType warningType, String description, Period emailInterval, Period smsInterval) {
        this.eventType = eventType;
        this.warningType = warningType;
        this.description = description;
        this.emailInterval = emailInterval;
        this.smsInterval = smsInterval;
    }

    public static List<NotificationIntervalDto> convertIntervalsToDto(List<NotificationInterval> intervals) {

        List<NotificationIntervalDto> result = new ArrayList<NotificationIntervalDto>();
        for (NotificationInterval i: intervals) {
            if (i.getWarningType() == null &&  i.getEventType() != null) {
                result.add(new NotificationIntervalDto(i.getEventType(), i.getWarningType(), i.toString(), i.getEmailInterval(), i.getSmsInterval()));
            }
            if (i.getWarningType() != null &&  i.getEventType() == null) {
                result.add(new NotificationIntervalDto(i.getEventType(), i.getWarningType(), i.toString(), i.getEmailInterval(), i.getSmsInterval()));
            }
        }
        return result;
    }

    public static List<NotificationIntervalData> convertIntervalsToData(List<NotificationIntervalDto> intervals) {

        return null;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Period getEmailInterval() {
        return emailInterval;
    }

    public void setEmailInterval(Period emailInterval) {
        this.emailInterval = emailInterval;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public Period getSmsInterval() {
        return smsInterval;
    }

    public void setSmsInterval(Period smsInterval) {
        this.smsInterval = smsInterval;
    }

    public WarningType getWarningType() {
        return warningType;
    }

    public void setWarningType(WarningType warningType) {
        this.warningType = warningType;
    }
}
