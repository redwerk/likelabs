package com.redwerk.likelabs.application.dto.user;

import com.redwerk.likelabs.domain.model.event.EventType;

import java.util.Set;

@Deprecated
public class UserData {

    private final String phone;

    private final String password;

    private final String email;

    private final boolean publishInSN;

    private final Set<EventType> enabledEvents;

    public UserData(String phone, String password, String email, boolean publishInSN, Set<EventType> enabledEvents) {
        this.phone = phone;
        this.password = password;
        this.email = email;
        this.publishInSN = publishInSN;
        this.enabledEvents = enabledEvents;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public boolean isPublishInSN() {
        return publishInSN;
    }

    public Set<EventType> getEnabledEvents() {
        return enabledEvents;
    }

}

