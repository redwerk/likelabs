package com.redwerk.likelabs.domain.model.event;

import com.redwerk.likelabs.domain.model.user.User;

import java.util.List;

public interface EventRepository {

    Event find(Long id);

    List<Event> findAll(User user, EventStatus status);

    void add(Event event);

    void remove(Event event);

}
