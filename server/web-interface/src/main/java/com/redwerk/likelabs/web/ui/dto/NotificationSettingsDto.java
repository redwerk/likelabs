package com.redwerk.likelabs.web.ui.dto;

import com.redwerk.likelabs.application.dto.NotificationIntervalData;
import com.redwerk.likelabs.application.template.MessageTemplateService;
import com.redwerk.likelabs.domain.model.event.EventType;
import com.redwerk.likelabs.domain.model.notification.NotificationInterval;
import com.redwerk.likelabs.domain.model.notification.Period;
import com.redwerk.likelabs.domain.model.notification.WarningType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import org.springframework.beans.factory.annotation.Autowired;


public class NotificationSettingsDto {

    private Period emailAbsent;
    private IntervalsSettingsDto email = new IntervalsSettingsDto();
    private IntervalsSettingsDto sms = new IntervalsSettingsDto();

    public NotificationSettingsDto() {
    }
    
    public NotificationSettingsDto(List<NotificationInterval> intervals) {

        for (NotificationInterval interval: intervals) {
            Period emailInterval = interval.getEmailInterval();
            Period smsInterval = interval.getSmsInterval();

            if (interval.getWarningType() != null) {
                switch (interval.getWarningType()) {
                    case EMAIL_IS_ABSENT: {
                        emailAbsent = smsInterval;
                        break;
                    }
                }
                continue;
            }

            switch (interval.getEventType()) {
                case CLIENT_REVIEW_CREATED: {
                    email.setPointCreatedReview(emailInterval);
                    sms.setPointCreatedReview(smsInterval);
                    break;
                }
                case USER_REVIEW_APPROVED: {
                    email.setUserApprovedReview(emailInterval);
                    sms.setUserApprovedReview(smsInterval);
                    break;
                }
                case USER_REVIEW_CREATED: {
                    email.setUserCreatedReview(emailInterval);
                    sms.setUserCreatedReview(smsInterval);
                    break;
                }
                default:
  
            }
        }
    }

    public List<NotificationIntervalData> getIntervals() {

        List<NotificationIntervalData> intervals = new ArrayList<NotificationIntervalData>();
        intervals.add(new NotificationIntervalData(null, WarningType.EMAIL_IS_ABSENT, Period.UNSUPPORTED, emailAbsent));
        intervals.add(new NotificationIntervalData(EventType.USER_REVIEW_CREATED, null, 
                email.getUserCreatedReview(),
                sms.getUserCreatedReview()));
        intervals.add(new NotificationIntervalData(EventType.USER_REVIEW_APPROVED, null, 
                email.getUserApprovedReview(),
                sms.getUserApprovedReview()));
        intervals.add(new NotificationIntervalData(EventType.CLIENT_REVIEW_CREATED, null, 
                email.getPointCreatedReview(),
                sms.getPointCreatedReview()));
        return intervals;

    }

    public Map<String, String> getAllItemsForOptions(MessageTemplateService messageTemplateService) {
        return getItemsForOptions(messageTemplateService, Period.NEVER, Period.IMMEDIATELY, Period.DAILY, Period.WEEKLY, Period.MONTHLY);
    }

    public Map<String, String> getItemsForOptions(MessageTemplateService messageTemplateService, Period ... periods) {
        Map<String, String> result = new HashMap<String, String>();
        for (Period p :periods) {
            result.put(p.toString(), messageTemplateService.getMessage("administrator.notification.period." + p.toString()));
        }
        return result;
    }

    public Period getEmailAbsent() {
        return emailAbsent;
    }

    public void setEmailAbsent(Period emailAbsent) {
        this.emailAbsent = emailAbsent;
    }

    public IntervalsSettingsDto getEmail() {
        return email;
    }

    public void setEmail(IntervalsSettingsDto email) {
        this.email = email;
    }

    public IntervalsSettingsDto getSms() {
        return sms;
    }

    public void setSms(IntervalsSettingsDto sms) {
        this.sms = sms;
    }
}