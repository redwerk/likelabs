package com.redwerk.likelabs.application.dto.user;

import com.redwerk.likelabs.domain.model.event.EventType;

import java.util.HashSet;
import java.util.Set;

public class UserSettingsData {

    private final boolean publishInSN;

    private final Set<EventType> enabledEvents;

    public UserSettingsData(boolean publishInSN, Set<EventType> enabledEvents) {
        this.publishInSN = publishInSN;
        this.enabledEvents = new HashSet<EventType>(enabledEvents);
    }

    public boolean isPublishInSN() {
        return publishInSN;
    }

    public Set<EventType> getEnabledEvents() {
        return enabledEvents;
    }

}
