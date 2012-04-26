package com.redwerk.likelabs.domain.model.company;

import javax.persistence.*;

@Embeddable
public class NotificationIntervals {

    private int emailInterval;

    private int smsInterval;


    public NotificationIntervals(int emailInterval, int smsInterval) {
        this.emailInterval = emailInterval;
        this.smsInterval = smsInterval;
    }

    public int getEmailInterval() {
        return emailInterval;
    }

    public int getSmsInterval() {
        return smsInterval;
    }

    // interface for JPA

    protected NotificationIntervals() {
    }

}
