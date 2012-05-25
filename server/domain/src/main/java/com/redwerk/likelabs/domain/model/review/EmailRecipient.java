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
@DiscriminatorValue("e")
class EmailRecipient extends Recipient {

    private String email;


    public EmailRecipient(Review review, String email) {
        super(review);
        this.email = email;
    }

    @Override
    protected boolean sendNotification(RecipientNotifier notifier) {
        Review review = getReview();
        return notifier.notifyByEmail(email, review.getId(), review.getAuthor().getName());
    }

    // overrides

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        EmailRecipient other = (EmailRecipient) obj;
        return new EqualsBuilder()
                .appendSuper(super.equals(obj))
                .append(email, other.email)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(email)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("email", email)
                .toString();
    }

    // interface for JPA

    protected  EmailRecipient() {
    }

}
