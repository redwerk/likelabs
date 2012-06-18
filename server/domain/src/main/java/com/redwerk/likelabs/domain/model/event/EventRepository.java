package com.redwerk.likelabs.domain.model.event;

import com.redwerk.likelabs.domain.model.query.Pager;
import com.redwerk.likelabs.domain.model.user.User;

import java.util.List;

public interface EventRepository {

    List<Event> findAll(EventStatus status);
    
    List<Event> findPending(User user, EventType type);

    void add(Event event);

    void remove(Event event);

}
