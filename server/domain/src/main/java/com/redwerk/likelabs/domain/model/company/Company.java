package com.redwerk.likelabs.domain.model.company;

import com.redwerk.likelabs.domain.model.SocialNetworkType;
import com.redwerk.likelabs.domain.model.company.exception.CompanyLogoTooBigException;
import com.redwerk.likelabs.domain.model.review.Review;
import com.redwerk.likelabs.domain.model.review.ReviewRegistrationAgent;
import com.redwerk.likelabs.domain.model.review.ReviewStatus;
import com.redwerk.likelabs.domain.model.user.User;
import com.redwerk.likelabs.domain.model.user.UserRepository;
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
    
    private static final int MAX_LOGO_ALLOWED_SIZE = 1048576;

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

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="company_social_page", joinColumns = @JoinColumn(name="company_id"))
    @Sort(type = SortType.NATURAL)
    private SortedSet<CompanySocialPage> pages = new TreeSet<CompanySocialPage>();

    @ManyToMany
    @JoinTable(
            name = "company_admin",
            joinColumns = @JoinColumn(name = "company_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> admins = new HashSet<User>();

    @ManyToMany
    @JoinTable(
            name = "sample_review",
            joinColumns = @JoinColumn(name = "company_id"),
            inverseJoinColumns = @JoinColumn(name = "review_id")
    )
    private Set<Review> sampleReviews = new HashSet<Review>();


    // constructors

    public Company(String name, String phone, String email, byte[] logo) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        setLogoInternal(logo);
    }

    public Company(String name, String phone, String email) {
        this(name, phone, email, null);
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
        setLogoInternal(logo);
    }

    private void setLogoInternal(byte[] logo) {
        if (logo.length > MAX_LOGO_ALLOWED_SIZE) {
            throw new CompanyLogoTooBigException(logo.length, MAX_LOGO_ALLOWED_SIZE);
        }
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

    public boolean addSocialPage(CompanySocialPage socialPage) {
        return pages.add(socialPage);
    }

    public boolean removeSocialPage(CompanySocialPage socialPage) {
        return pages.remove(socialPage);
    }

    public void clearSocialPages() {
        pages.clear();
    }

    // administrators

    public Set<User> getAdmins() {
        return Collections.unmodifiableSet(admins);
    }
    
    public boolean addAdmin(User admin) {
        return admins.add(admin);
    }

    public boolean removeAdmin(User admin, CompanyRepository companyRepository, UserRepository userRepository) {
        boolean isRemoved = admins.remove(admin);
        if (isRemoved) {
            if (companyRepository.getCount(admin) == 0) {
                userRepository.remove(admin);
            }
        }
        return isRemoved;
    }

    public void clearAdmins() {
        admins.clear();
    }

    // sample reviews

    public Set<Review> getSampleReviews(){
        return Collections.unmodifiableSet(sampleReviews);
    }

    public boolean addSampleReview(Review review) {
        return sampleReviews.add(review);
    }

    public boolean removeSampleReview(Review review) {
        return sampleReviews.remove(review);
    }

    public void clearSampleReviews() {
        sampleReviews.clear();
    }
    
    // reviews
    
    public void registerReview(Review review, ReviewRegistrationAgent registrationAgent) {
        if (!moderateReviews) {
            registrationAgent.setReviewStatus(review, ReviewStatus.APPROVED);
        }
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
                .append("admins", admins)
                .append("sampleReviews", sampleReviews)
                .toString();
    }

    // interface for JPA

    protected Company() {
    }

}
