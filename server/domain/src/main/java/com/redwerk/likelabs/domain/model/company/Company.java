package com.redwerk.likelabs.domain.model.company;

import com.redwerk.likelabs.domain.model.event.EventType;
import com.redwerk.likelabs.domain.model.user.User;
import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;

import javax.persistence.*;
import java.util.*;

import static com.redwerk.likelabs.domain.model.event.EventType.*;

@Entity
public class Company {
    
    private static final int DEFAULT_EMAIL_INTERVAL = 1;

    private static final int DEFAULT_SMS_INTERVAL = 7;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;

    private String phone;

    private String email;

    private boolean moderateReviews;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] logo;

    @ElementCollection
    @CollectionTable(name="company_social_account", joinColumns = @JoinColumn(name="company_id"))
    @Sort(type = SortType.NATURAL)
    private SortedSet<CompanySocialAccount> accounts = new TreeSet<CompanySocialAccount>();

    @ElementCollection
    @CollectionTable(name="notification_intervals", joinColumns = @JoinColumn(name="company_id"))
    @MapKeyColumn(name = "event_type")
    private Map<EventType, NotificationIntervals> intervals =
            new HashMap<EventType, NotificationIntervals>() {{
                for (EventType eventType: EventType.values()) {
                    put(eventType, new NotificationIntervals(DEFAULT_EMAIL_INTERVAL, DEFAULT_SMS_INTERVAL));
                }
            }};

    @ManyToMany
    @JoinTable(
            name = "company_admin",
            joinColumns = @JoinColumn(name = "company_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "user_id", nullable = false)
    )
    private Set<User> admins = new HashSet<User>();

}
