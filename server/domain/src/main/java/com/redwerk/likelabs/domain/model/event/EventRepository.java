package com.redwerk.likelabs.domain.model.event;

import com.redwerk.likelabs.domain.model.user.User;

import java.util.List;

public interface EventRepository {

    List<Event> findAll(User user, EventStatus status, int offset, int count);

    void add(Event event);

    void remove(Event event);

}
