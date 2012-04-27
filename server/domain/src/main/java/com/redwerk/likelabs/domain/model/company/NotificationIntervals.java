package com.redwerk.likelabs.domain.model.company;

import com.redwerk.likelabs.domain.model.event.EventType;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.persistence.*;

@Embeddable
public class NotificationIntervals {

    private EventType eventType;

    private int emailInterval;

    private int smsInterval;

    public NotificationIntervals(EventType eventType, int emailInterval, int smsInterval) {
        this.eventType = eventType;
        this.emailInterval = emailInterval;
        this.smsInterval = smsInterval;
    }

    public EventType getEventType() {
        return eventType;
    }

    public int getEmailInterval() {
        return emailInterval;
    }

    public int getSmsInterval() {
        return smsInterval;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        NotificationIntervals other = (NotificationIntervals) obj;
        return new EqualsBuilder()
                .append(eventType, other.eventType)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(eventType)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("eventType", eventType)
                .append("emailInterval", emailInterval)
                .append("smsInterval", smsInterval)
                .toString();
    }

    // interface for JPA

    protected NotificationIntervals() {
    }

}
