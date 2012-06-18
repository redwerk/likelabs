package com.redwerk.likelabs.domain.model.event;

import com.redwerk.likelabs.domain.model.review.Review;
import com.redwerk.likelabs.domain.model.user.User;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "event")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private EventType type;

    @Column(name = "created_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDT;

    @Column(name = "notified_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date notifiedDT;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "review_id")
    private Review review;


    // constructors

    public Event(EventType type, User user, Review review) {
        this.type = type;
        this.user = user;
        this.review = review;
        this.createdDT = new Date();
    }

    // accessors

    public Long getId() {
        return id;
    }

    public EventType getType() {
        return type;
    }

    public User getUser() {
        return user;
    }

    public Review getReview() {
        return review;
    }

    public Date getCreatedDT() {
        return createdDT;
    }

    public Date getNotifiedDT() {
        return notifiedDT;
    }

    public boolean isNotified() {
        return notifiedDT != null;
    }

    // modifiers

    public void markAsNotified() {
        notifiedDT = new Date();
    }

    // overrides

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Event other = (Event) obj;
        return new EqualsBuilder()
                .append(type, other.type)
                .append(createdDT, other.createdDT)
                .append(user, other.user)
                .append(review, other.review)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(type)
                .append(createdDT)
                .append(user)
                .append(review)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("type", type)
                .append("createdDT", createdDT)
                .append("notifiedDT", notifiedDT)
                .append("user", user)
                .append("review", review)
                .toString();
    }
    // interface for JPA

    protected Event() {
    }

}
