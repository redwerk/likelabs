package com.redwerk.likelabs.domain.service;

import com.redwerk.likelabs.domain.model.event.Event;
import com.redwerk.likelabs.domain.model.user.User;

import java.util.List;

public interface UserNotifier {

    boolean sendEmail(User user, List<Event> events);

    boolean sendSms(User user, List<Event> events);
    
    boolean sendWarning(User user);

}
