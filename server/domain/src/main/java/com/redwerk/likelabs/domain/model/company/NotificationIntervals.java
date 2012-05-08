package com.redwerk.likelabs.domain.model.company;

import com.redwerk.likelabs.domain.model.event.EventType;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.persistence.*;

@Embeddable
public class NotificationIntervals {

    @Column(name = "email_interval")
    private int emailInterval;

    @Column(name = "sms_interval")
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

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        NotificationIntervals other = (NotificationIntervals) obj;
        return new EqualsBuilder()
                .append(emailInterval, other.emailInterval)
                .append(smsInterval, other.smsInterval)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(emailInterval)
                .append(smsInterval)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("emailInterval", emailInterval)
                .append("smsInterval", smsInterval)
                .toString();
    }

    // interface for JPA

    protected NotificationIntervals() {
    }

}
