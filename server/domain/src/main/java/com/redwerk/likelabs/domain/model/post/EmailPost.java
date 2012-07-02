package com.redwerk.likelabs.domain.model.post;

import com.redwerk.likelabs.domain.model.review.Review;
import com.redwerk.likelabs.domain.model.user.User;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "email_post")
@PrimaryKeyJoinColumn(name="post_id")
@DiscriminatorValue("e")
public class EmailPost extends Post {
    
    private String email;


    // constructors

    public EmailPost(Review review, User recipient, String email) {
        super(review, recipient);
        this.email = email;
    }

    // accessors

    public String getEmail() {
        return email;
    }

    // overrides

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        EmailPost other = (EmailPost) obj;
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

    protected  EmailPost() {
    }

}
