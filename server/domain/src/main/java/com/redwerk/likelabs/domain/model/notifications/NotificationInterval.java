package com.redwerk.likelabs.domain.model.notifications;

import com.redwerk.likelabs.domain.model.event.EventType;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.persistence.*;

@Entity
@Table(name = "notification_interval")
public class NotificationInterval {
    
    private static final int UNDEFINED_PERIOD = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_type")
    private EventType eventType;

    @Column(name = "warning_type")
    private WarningType warningType;
    
    @Column(name = "email_interval")
    private int emailInterval = UNDEFINED_PERIOD;

    @Column(name = "sms_interval")
    private int smsInterval = UNDEFINED_PERIOD;


    public NotificationInterval(EventType eventType) {
        this.eventType = eventType;
    }

    public NotificationInterval(WarningType warningType) {
        this.warningType = warningType;
    }

    public EventType getEventType() {
        return eventType;
    }

    public WarningType getWarningType() {
        return warningType;
    }

    public int getEmailInterval() {
        return emailInterval;
    }

    public int getSmsInterval() {
        return smsInterval;
    }

    public void setEmailInterval(int emailInterval) {
        if (emailInterval <= 0 && emailInterval != UNDEFINED_PERIOD) {
            throw new IllegalArgumentException("Incorrect emailInterval");
        }
        this.emailInterval = emailInterval;
    }

    public void setSmsInterval(int smsInterval) {
        if (smsInterval <= 0 && smsInterval != UNDEFINED_PERIOD) {
            throw new IllegalArgumentException("Incorrect smsInterval");
        }
        this.smsInterval = smsInterval;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        NotificationInterval other = (NotificationInterval) obj;
        return new EqualsBuilder()
                .append(eventType, other.eventType)
                .append(warningType, other.warningType)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(eventType)
                .append(warningType)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("eventType", eventType)
                .append("warningType", warningType)
                .append("emailInterval", emailInterval)
                .append("smsInterval", smsInterval)
                .toString();
    }

    // interface for JPA

    protected NotificationInterval() {
    }

}
