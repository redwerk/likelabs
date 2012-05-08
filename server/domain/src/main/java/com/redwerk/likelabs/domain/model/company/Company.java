package com.redwerk.likelabs.domain.model.company;

import com.redwerk.likelabs.domain.model.SocialNetworkType;
import com.redwerk.likelabs.domain.model.event.EventType;
import com.redwerk.likelabs.domain.model.review.Review;
import com.redwerk.likelabs.domain.model.user.User;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "company")
public class Company {
    
    private static final int DEFAULT_EMAIL_INTERVAL = 1;

    private static final int DEFAULT_SMS_INTERVAL = 7;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;

    private String phone;

    private String email;

    @Column(name = "moderate_reviews")
    private boolean moderateReviews = true;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] logo;

    @ElementCollection
    @CollectionTable(name="company_social_account", joinColumns = @JoinColumn(name="company_id"))
    @Sort(type = SortType.NATURAL)
    private SortedSet<CompanySocialPage> pages = new TreeSet<CompanySocialPage>();

    @ElementCollection
    @CollectionTable(name="notification_intervals", joinColumns = @JoinColumn(name="company_id"))
    @MapKeyColumn(name = "event_type")
    private Map<EventType, NotificationIntervals> intervals =
            new HashMap<EventType, NotificationIntervals>() {{
                for (EventType et: EventType.values()) {
                    put(et, new NotificationIntervals(DEFAULT_EMAIL_INTERVAL, DEFAULT_SMS_INTERVAL));
                }
            }};

    @ManyToMany
    @JoinTable(
            name = "company_admin",
            joinColumns = @JoinColumn(name = "company_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> admins;

    @ManyToMany
    @JoinTable(
            name = "sample_review",
            joinColumns = @JoinColumn(name = "company_id"),
            inverseJoinColumns = @JoinColumn(name = "review_id")
    )
    private Set<Review> sampleReviews = new HashSet<Review>();


    // constructors

    public Company(String name, String phone, String email, Set<User> admins) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.admins = new HashSet<User>(admins);
    }

    // accessors

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public boolean isModerateReviews() {
        return moderateReviews;
    }

    public byte[] getLogo() {
        return logo;
    }

    // modifiers

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setModerateReviews(boolean moderateReviews) {
        this.moderateReviews = moderateReviews;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }
    
    // social accounts

    public Set<CompanySocialPage> getSocialPages() {
        return Collections.unmodifiableSet(pages);
    }

    public Set<CompanySocialPage> getSocialPages(SocialNetworkType networkType) {
        Set<CompanySocialPage> typedAccounts = new HashSet<CompanySocialPage>();
        for (CompanySocialPage sp: pages) {
            if (sp.getType() == networkType) {
                typedAccounts.add(sp);
            }
        }
        return Collections.unmodifiableSet(typedAccounts);
    }

    public void addSocialPage(CompanySocialPage socialPage) {
        assert !pages.contains(socialPage);
        pages.add(socialPage);
    }

    public void removeSocialPage(CompanySocialPage socialPage) {
        assert pages.contains(socialPage);
        pages.remove(socialPage);
    }

    public void clearSocialPages() {
        pages.clear();
    }

    // notification intervals

    public Set<NotificationIntervals> getAllNotificationIntervals() {
        return Collections.unmodifiableSet(new HashSet<NotificationIntervals>(intervals.values()));
    }

    public NotificationIntervals getNotificationIntervals(EventType eventType) {
        return intervals.get(eventType);
    }

    public void setNotificationIntervals(EventType eventType, NotificationIntervals newIntervals) {
        intervals.put(eventType, newIntervals);
    }

    // administrators

    public Set<User> getAdmins() {
        return Collections.unmodifiableSet(admins);
    }
    
    public void addAdmin(User admin) {
        assert !admins.contains(admin);
        admins.add(admin);
    }

    public void removeAdmin(User admin) {
        assert admins.contains(admin);
        admins.remove(admin);
    }

    public void clearAdmins() {
        admins.clear();
    }

    // sample reviews

    public Set<Review> getSampleReview(){
        return Collections.unmodifiableSet(sampleReviews);
    }

    public void addSampleReview(Review review) {
        assert !sampleReviews.contains(review);
        sampleReviews.add(review);
    }

    public void removeSampleReview(Review review) {
        assert sampleReviews.contains(review);
        sampleReviews.remove(review);
    }

    public void clearSampleReviews() {
        sampleReviews.clear();
    }

    // overrides

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Company other = (Company) obj;
        return new EqualsBuilder()
                .append(name, other.name)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(name)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("name", phone)
                .append("phone", phone)
                .append("email", email)
                .append("moderateReviews", moderateReviews)
                .append("hasLogo", logo != null)
                .append("pages", pages)
                .append("intervals", intervals)
                .append("admins", admins)
                .append("sampleReviews", sampleReviews)
                .toString();
    }

    // interface for JPA

    protected Company() {
    }

}
