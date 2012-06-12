package com.redwerk.likelabs.domain.model.notification;

import com.redwerk.likelabs.domain.model.event.EventType;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.persistence.*;

@Entity
@Table(name = "notification_interval")
public class NotificationInterval {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_type")
    private EventType eventType;

    @Column(name = "warning_type")
    private WarningType warningType;
    
    @Column(name = "email_interval")
    private Period emailInterval = Period.DAILY;

    @Column(name = "sms_interval")
    private Period smsInterval = Period.DAILY;


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

    public Period getEmailInterval() {
        return emailInterval;
    }

    public Period getSmsInterval() {
        return smsInterval;
    }

    public void setEmailInterval(Period emailInterval) {
        Validate.notNull(emailInterval, "emailInterval cannot be null");
        Validate.isTrue((this.emailInterval != Period.UNSUPPORTED) || (emailInterval == Period.UNSUPPORTED),
                "email interval is not supported");
        this.emailInterval = emailInterval;
    }

    public void setSmsInterval(Period smsInterval) {
        Validate.notNull(smsInterval, "smsInterval cannot be null");
        Validate.isTrue((this.smsInterval != Period.UNSUPPORTED) || (smsInterval == Period.UNSUPPORTED),
                "smsInterval interval is not supported");
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
