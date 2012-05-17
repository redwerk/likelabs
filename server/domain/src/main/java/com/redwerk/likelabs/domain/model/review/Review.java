package com.redwerk.likelabs.domain.model.review;

import com.redwerk.likelabs.domain.model.company.Company;
import com.redwerk.likelabs.domain.model.photo.Photo;
import com.redwerk.likelabs.domain.model.point.Point;
import com.redwerk.likelabs.domain.model.review.exception.NotAuthorizedReviewUpdateException;
import com.redwerk.likelabs.domain.model.review.exception.UpdateType;
import com.redwerk.likelabs.domain.model.user.User;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "review")
public class Review {
    
    private static final int MAX_RECIPIENTS_NUMBER = 5;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;

    @ManyToOne
    @JoinColumn(name = "photo_id")
    private Photo photo;

    @ManyToOne
    @JoinColumn(name = "point_id")
    private Point point;
    
    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @Column(name = "created_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDT;

    @Column(name = "modified_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDT;

    private ReviewStatus status;

    @Column(name = "published_in_company_sn")
    private boolean publishedInCompanySN;
    
    @ManyToOne
    @JoinColumn(name = "moderator_id")
    private User moderator;

    @Column(name = "moderated_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date moderatedDT;

    @ElementCollection
    @CollectionTable(name="review_recipient", joinColumns = @JoinColumn(name="review_id"))
    private Set<Recipient> recipients;


    // constructors

    public Review(String message, Photo photo, Point point, User author, Set<Recipient> recipients) {
        if (recipients.size() > MAX_RECIPIENTS_NUMBER) {
            throw new IllegalArgumentException("too many recipients for review");
        }
        this.message = message;
        this.photo = photo;
        this.point = point;
        this.author = author;
        this.createdDT = new Date();
        this.status = ReviewStatus.PENDING;
        this.publishedInCompanySN = false;
        this.recipients = new HashSet<Recipient>(recipients);
    }

    // accessors

    public Long getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public Photo getPhoto() {
        return photo;
    }

    public Point getPoint() {
        return point;
    }

    public User getAuthor() {
        return author;
    }

    public Date getCreatedDT() {
        return createdDT;
    }

    public Date getModifiedDT() {
        return modifiedDT;
    }

    public ReviewStatus getStatus() {
        return status;
    }

    public boolean isPublishedInCompanySN() {
        return publishedInCompanySN;
    }

    public User getModerator() {
        return moderator;
    }

    public Date getModeratedDT() {
        return moderatedDT;
    }

    public Set<Recipient> getRecipients() {
        return Collections.unmodifiableSet(recipients);
    }

    // modifiers

    public boolean setMessage(String message, User user) {
        if (!author.equals(user)) {
            throw new NotAuthorizedReviewUpdateException(user, this, UpdateType.UPDATE_MESSAGE);
        }
        if (message.equals(this.message)) {
            return false;
        }
        this.message = message;
        this.modifiedDT = new Date();
        return true;
    }

    public boolean setStatus(ReviewStatus status, User moderator) {
        if (!canModerate(moderator)) {
            throw new NotAuthorizedReviewUpdateException(moderator, this, UpdateType.UPDATE_STATUS);
        }
        if (this.status.equals(status)) {
            return false;
        }
        this.status = status;
        markAsModerated(moderator);
        return true;
    }

    public boolean publishInCompanySN(User moderator) {
        if (!canModerate(moderator)) {
            throw new NotAuthorizedReviewUpdateException(moderator, this, UpdateType.APPROVE_FOR_COMPANY_PAGE);
        }
        if (publishedInCompanySN) {
            return false;
        }
        // TODO: publish on company pages
        publishedInCompanySN = true;
        markAsModerated(moderator);
        return true;
    }

    public boolean setSampleStatus(boolean useAsSample, User moderator) {
        if (!canModerate(moderator)) {
            throw new NotAuthorizedReviewUpdateException(moderator, this, UpdateType.UPDATE_STATUS);
        }
        Company company = point.getCompany();
        return useAsSample ? company.addSampleReview(this) : company.removeSampleReview(this);
    }

    private void markAsModerated(User moderator) {
        this.moderator = moderator;
        this.moderatedDT = new Date();
    }
    
    private boolean canModerate(User user) {
        return point.getCompany().getAdmins().contains(moderator);
    }

    // overrides

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Review other = (Review) obj;
        return new EqualsBuilder()
                .append(point, other.point)
                .append(author, other.author)
                .append(createdDT, other.createdDT)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(point)
                .append(author)
                .append(createdDT)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("message", message)
                .append("photo", photo)
                .append("point", point)
                .append("author", author)
                .append("createdDT", createdDT)
                .append("modifiedDT", modifiedDT)
                .append("status", status)
                .append("publishedInCompanySN", publishedInCompanySN)
                .append("moderator", moderator)
                .append("moderatedDT", moderatedDT)
                .append("recipients", recipients)
                .toString();
    }

    // interface for JPA

    protected Review() {
    }

}
