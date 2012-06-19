package com.redwerk.likelabs.domain.model.user;

import com.redwerk.likelabs.domain.model.SocialNetworkType;
import com.redwerk.likelabs.domain.model.event.Event;
import com.redwerk.likelabs.domain.model.event.EventRepository;
import com.redwerk.likelabs.domain.model.event.EventType;
import com.redwerk.likelabs.domain.model.review.Review;
import com.redwerk.likelabs.domain.model.user.exception.AccountNotExistsException;
import com.redwerk.likelabs.domain.model.user.exception.DuplicatedAccountException;
import com.redwerk.likelabs.domain.service.notification.NotificationProcessor;
import com.redwerk.likelabs.domain.service.sn.GatewayFactory;
import com.redwerk.likelabs.domain.service.sn.ImageSourceFactory;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "user")
public class User {

    private static final String ANONYMOUS_USER_NAME = "Anonymous";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String phone;

    private String password;

    @Column(name = "status")
    private UserStatus status;

    private String email;

    @Column(name = "system_admin")
    private boolean systemAdmin = false;

    @Column(name = "publish_in_sn")
    private boolean publishInSN = true;

    @Column(name = "created_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDT = new Date();

    @Column(name = "notified_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date notifiedDT;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="user_social_account", joinColumns = @JoinColumn(name="user_id"))
    @Sort(type = SortType.NATURAL)
    private SortedSet<UserSocialAccount> accounts = new TreeSet<UserSocialAccount>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="user_event_type", joinColumns = @JoinColumn(name="user_id"))
    @Column(name = "event_type")
    private Set<EventType> enabledEvents;

    // constructors

    protected User(String phone, String password, boolean isActive) {
        this.phone = phone;
        this.password = password;
        this.status = isActive ? UserStatus.ACTIVE : UserStatus.NOT_ACTIVATED;
        this.enabledEvents =  new HashSet<EventType>(Arrays.asList(EventType.values()));
    }

    // accessors
    
    public boolean isActive() {
        return (status == UserStatus.ACTIVE);
    }

    public boolean isDeleted() {
        return (status == UserStatus.DELETED);
    }

    public UserStatus getStatus() {
        return status;
    }

    public boolean isAnonymous() {
         return accounts.size() == 0;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return isAnonymous() ? ANONYMOUS_USER_NAME : accounts.first().getName();
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public boolean hasEmail() {
        return StringUtils.isNotBlank(email);
    }

    public boolean isSystemAdmin() {
        return systemAdmin;
    }

    public boolean isPublishInSN() {
        return publishInSN;
    }

    public Date getNotifiedDT() {
        return notifiedDT;
    }

    public Date getCreatedDT() {
        return createdDT;
    }

    public Set<EventType> getEnabledEvents() {
        return Collections.unmodifiableSet(enabledEvents);
    }
    
    // modifiers

    public void activate() {
        if (status != UserStatus.NOT_ACTIVATED) {
            throw new IllegalStateException("user is already activated");
        }
        status = UserStatus.ACTIVE;
    }

    protected void archive() {
        if (status != UserStatus.ACTIVE) {
            throw new IllegalStateException("user " + phone + " is not active");
        }
        status = UserStatus.DELETED;
    }

    public void restore() {
        if (status != UserStatus.DELETED) {
            throw new IllegalStateException("user " + phone + " is not archived");
        }
        status = UserStatus.ACTIVE;
    }

    public void markAsNotified() {
        notifiedDT = new Date();
    }

    public void setPhone(String phone) {
        if (StringUtils.isEmpty(phone)) {
            throw new IllegalArgumentException("phone cannot by empty");
        }
        this.phone = phone;
    }

    public void setPassword(String password) {
        if (StringUtils.isEmpty(password)) {
            throw new IllegalArgumentException("password cannot by empty");
        }
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSystemAdmin(boolean systemAdmin) {
        this.systemAdmin = systemAdmin;
    }

    public void setPublishInSN(boolean publishInSN) {
        this.publishInSN = publishInSN;
    }

    public void setEnabledEvents(Set<EventType> enabledEvents) {
        this.enabledEvents.clear();
        for (EventType et: enabledEvents) {
            this.enabledEvents.add(et);
        }
    }

    // social accounts

    public List<UserSocialAccount> getAccounts() {
        return Collections.unmodifiableList(new ArrayList<UserSocialAccount>(accounts));
    }

    public UserSocialAccount findAccount(SocialNetworkType networkType) {
        for (UserSocialAccount sa: accounts) {
            if (sa.getType() == networkType) {
                return sa;
            }
        }
        return null;
    }

    /*
    public UserSocialAccount getPrimaryAccount() {
        return isAnonymous() ? null : accounts.first();
    }
    */

    public void addAccount(UserSocialAccount newAccount) {
        if (findAccount(newAccount.getType()) != null) {
            throw new DuplicatedAccountException(this, newAccount.getType());
        }
        accounts.add(newAccount);
    }

    public void removeAccount(SocialNetworkType networkType) {
        UserSocialAccount account = findAccount(networkType);
        if (account == null) {
            throw new AccountNotExistsException(this, networkType);
        }
        accounts.remove(account);
    }
    
    // reviews
    
    public void registerOwnReview(Review review, EventRepository eventRepository, NotificationProcessor notificationProcessor,
                                  GatewayFactory gatewayFactory, ImageSourceFactory imageSourceFactory) {
        generateEvent(EventType.USER_REVIEW_CREATED, eventRepository, review, notificationProcessor);
        publishInSN(review, gatewayFactory, imageSourceFactory);
    }

    public void registerClientReview(Review review, EventRepository eventRepository,
                                     NotificationProcessor notificationProcessor) {
        generateEvent(EventType.CLIENT_REVIEW_CREATED, eventRepository, review, notificationProcessor);
    }

    public void registerReviewApproval(Review review, EventRepository eventRepository,
                                       NotificationProcessor notificationProcessor) {
        generateEvent(EventType.USER_REVIEW_APPROVED, eventRepository, review, notificationProcessor);
    }

    private void generateEvent(EventType eventType, EventRepository eventRepository, Review review,
                               NotificationProcessor notificationProcessor) {
        if (enabledEvents.contains(eventType)) {
            Event event = new Event(eventType, this, review);
            eventRepository.add(event);
            notificationProcessor.processEvent(event);
        }
    }

    private void publishInSN(Review review, GatewayFactory gatewayFactory, ImageSourceFactory imageSourceFactory) {
        if (publishInSN) {
            for (UserSocialAccount account: accounts) {
                account.publishReview(review, gatewayFactory, imageSourceFactory);
            }
        }
    }

    // overrides

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User other = (User) obj;
        return new EqualsBuilder()
                .append(phone, other.phone)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(phone)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("phone", phone)
                .append("password", password)
                .append("status", status)
                .append("email", email)
                .append("systemAdmin", systemAdmin)
                .append("publishInSN", publishInSN)
                .append("createdDT", createdDT)
                .append("notifiedDT", notifiedDT)
                .append("accounts", accounts)
                .append("enabledEvents", enabledEvents)
                .toString();
    }

    // interface for JPA

    protected User() {
    }

}
