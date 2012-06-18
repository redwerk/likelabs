package com.redwerk.likelabs.application.notification;

import com.redwerk.likelabs.domain.model.event.Event;
import com.redwerk.likelabs.domain.model.user.User;
import com.redwerk.likelabs.domain.service.UserNotifier;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserNotifierImpl implements UserNotifier {

    @Override
    public boolean sendEmail(User user, List<Event> events) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean sendSms(User user, List<Event> events) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean sendWarning(User user) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

}
