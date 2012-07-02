package com.redwerk.likelabs.domain.model.post;

import com.redwerk.likelabs.domain.model.review.Review;
import com.redwerk.likelabs.domain.model.user.User;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "post")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING, length = 1)
public abstract class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "review_id")
    private Review review;

    @ManyToOne
    @JoinColumn(name = "recipient_id")
    private User recipient;

    @Column(name = "created_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;


    // constructors

    protected Post(Review review, User recipient) {
        this.review = review;
        this.recipient = recipient;
        this.created = new Date();
    }

    // accessors

    public Long getId() {
        return id;
    }

    public Review getReview() {
        return review;
    }

    public User getRecipient() {
        return recipient;
    }

    public Date getCreated() {
        return created;
    }

    // overrides

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Post other = (Post) obj;
        return new EqualsBuilder()
                .append(review, other.review)
                .append(recipient, other.recipient)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(review)
                .append(recipient)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("review", review)
                .append("recipient", recipient)
                .append("created", created)
                .toString();
    }

    // interface for JPA

    protected Post() {
    }

}
