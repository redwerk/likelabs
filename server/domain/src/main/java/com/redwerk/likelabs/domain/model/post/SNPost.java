package com.redwerk.likelabs.domain.model.post;

import com.redwerk.likelabs.domain.model.SocialNetworkType;
import com.redwerk.likelabs.domain.model.review.Review;
import com.redwerk.likelabs.domain.model.user.User;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.persistence.*;

@Entity
@Table(name = "sn_post")
@PrimaryKeyJoinColumn(name="post_id")
@DiscriminatorValue("s")
public class SNPost extends Post {

    @Column(name = "sn_type")
    private SocialNetworkType snType;

    @Column(name = "sn_post_id")
    private String snPostId;


    // constructors

    public SNPost(Review review, User recipient, SocialNetworkType snType, String snPostId) {
        super(review, recipient);
        this.snType = snType;
        this.snPostId = snPostId;
    }

    // accessors

    public SocialNetworkType getSnType() {
        return snType;
    }

    public String getSnPostId() {
        return snPostId;
    }

    // overrides

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SNPost other = (SNPost) obj;
        return new EqualsBuilder()
                .appendSuper(super.equals(obj))
                .append(snType, other.snType)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(snType)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("snType", snType)
                .append("snPostId", snPostId)
                .toString();
    }

    // interface for JPA

    protected  SNPost() {
    }
}
