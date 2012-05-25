package com.redwerk.likelabs.domain.model.review;

import com.redwerk.likelabs.domain.service.RecipientNotifier;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "review_email_recipient")
@DiscriminatorValue("s")
class SmsRecipient extends Recipient {

    private static final Logger LOGGER = Logger.getLogger(SmsRecipient.class);

    private String phone;


    public SmsRecipient(Review review, String phone) {
        super(review);
        this.phone = phone;
    }

    @Override
    protected boolean sendNotification(RecipientNotifier notifier) {
        try {
            Review review = getReview();
            notifier.notifyBySms(phone, review.getId(), review.getAuthor().getName());
        }
        // TODO: use concrete exception class
        catch (Exception e) {
            LOGGER.error("cannot notify review recipient by SMS " + phone, e);
            return false;
        }
        return true;
    }

    // overrides

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SmsRecipient other = (SmsRecipient) obj;
        return new EqualsBuilder()
                .appendSuper(super.equals(obj))
                .append(phone, other.phone)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(phone)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("phone", phone)
                .toString();
    }

    // interface for JPA

    protected  SmsRecipient() {
    }

}
