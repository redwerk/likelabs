package com.redwerk.likelabs.domain.service;

import com.redwerk.likelabs.domain.model.event.Event;
import com.redwerk.likelabs.domain.model.event.EventType;
import com.redwerk.likelabs.domain.model.notification.WarningType;
import com.redwerk.likelabs.domain.model.user.User;

import java.util.List;

public interface UserNotifier {

    boolean sendEmail(User user, EventType eventType, List<Event> events);

    boolean sendSms(User user, EventType eventType, List<Event> events);
    
    boolean sendWarning(User user, WarningType warningType);

}
