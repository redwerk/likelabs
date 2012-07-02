package com.redwerk.likelabs.application.dto.user;

import com.redwerk.likelabs.domain.model.event.EventType;

import java.util.HashSet;
import java.util.Set;

public class UserSettingsData {

    private final boolean postToSn;

    private final boolean postToEmail;

    private final Set<EventType> enabledEvents;

    public UserSettingsData(boolean postToSn, boolean postToEmail, Set<EventType> enabledEvents) {
        this.postToSn = postToSn;
        this.postToEmail = postToEmail;
        this.enabledEvents = new HashSet<EventType>(enabledEvents);
    }

    public boolean isPostToSn() {
        return postToSn;
    }

    public boolean isPostToEmail() {
        return postToEmail;
    }

    public Set<EventType> getEnabledEvents() {
        return enabledEvents;
    }

}
