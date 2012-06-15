package com.redwerk.likelabs.domain.model.review;

import com.redwerk.likelabs.domain.service.RecipientNotifier;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;

import javax.persistence.*;

@Entity
@Table(name = "review_recipient")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING, length = 1)
public abstract class Recipient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "is_notified")
    private boolean notified = false;

    @ManyToOne
    @JoinColumn(name = "review_id")
    private Review review;


    // constructors

    protected Recipient(Review review) {
        this.review = review;
    }

    // business methods

    public Long getId() {
        return id;
    }

    public boolean isNotified() {
        return notified;
    }

    public boolean notify(RecipientNotifier notifier) {
        if (notified) {
            throw new IllegalStateException("recipient is already notified");
        }
        return sendNotification(notifier);
    }

    protected abstract boolean sendNotification(RecipientNotifier notifier);

    protected Review getReview() {
        return review;
    }

    // overrides

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Recipient other = (Recipient) obj;
        return new EqualsBuilder()
                .append(review, other.review)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(review)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("notified", notified)
                .append("review", review)
                .toString();
    }

    // interface for JPA

    protected Recipient() {
    }

}
