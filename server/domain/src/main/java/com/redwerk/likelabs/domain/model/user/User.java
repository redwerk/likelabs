package com.redwerk.likelabs.domain.model.user;

import com.redwerk.likelabs.domain.model.SocialAccountType;
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

    private String email;

    private boolean systemAdmin = false;

    @Column(name = "publish_in_sn")
    private boolean publishInSN = true;

    private boolean notifyIfClient = true;

    @Column(name = "created_dt")
    private Date createdDT = new Date();

    @Column(name = "activated_dt")
    private Date activatedDT;

    @Column(name = "notified_dt")
    private Date notifiedDT;

    @ElementCollection
    @CollectionTable(name="user_social_account", joinColumns = @JoinColumn(name="user_id"))
    @Sort(type = SortType.NATURAL)
    private SortedSet<UserSocialAccount> accounts = new TreeSet<UserSocialAccount>();


    // constructors

    public User(String phone, String password) {
        this.phone = phone;
        this.password = password;
    }

    // accessors
    
    public boolean isActive() {
        return activatedDT != null;
    }
    
    public boolean isAnonymous() {
         return accounts.size() == 0;
    }

    public String getName() {
        return isAnonymous() ? ANONYMOUS_USER_NAME : accounts.first().getName();
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public boolean isSystemAdmin() {
        return systemAdmin;
    }

    public boolean isPublishInSN() {
        return publishInSN;
    }

    public boolean isNotifyIfClient() {
        return notifyIfClient;
    }

    public Date getNotifiedDT() {
        return notifiedDT;
    }

    public Date getCreatedDT() {
        return createdDT;
    }

    public Date getActivatedDT() {
        return activatedDT;
    }

    // modifiers

    public void activate() {
        assert activatedDT == null;
        activatedDT = new Date();
    }

    public void MarkAsNotified() {
        notifiedDT = new Date();
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPassword(String oldPassword, String newPassword) {
        if (!password.equals(oldPassword)) {
            throw new WrongUserPasswordException(this, oldPassword);
        }
        this.password = newPassword;
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

    public void setNotifyIfClient(boolean notifyIfClient) {
        this.notifyIfClient = notifyIfClient;
    }

    // social accounts

    public UserSocialAccount getPrimaryAccount() {
        assert !isAnonymous();
        return accounts.first();
    }

    public UserSocialAccount accountOfType(SocialAccountType accountType) {
        for (UserSocialAccount sa: accounts) {
            if (sa.getType() == accountType) {
                return sa;
            }
        }
        return null;
    }

    public List<UserSocialAccount> getAllAccounts() {
        assert !isAnonymous();
        return Collections.unmodifiableList(new ArrayList<UserSocialAccount>(accounts));
    }

    public void addAccount(UserSocialAccount socialAccount) {
        assert !accounts.contains(socialAccount);
        accounts.add(socialAccount);
    }

    public void removeAccount(UserSocialAccount socialAccount) {
        assert accounts.contains(socialAccount);
        accounts.remove(socialAccount);
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
                .append("email", email)
                .append("systemAdmin", systemAdmin)
                .append("publishInSN", publishInSN)
                .append("notifyIfClient", notifyIfClient)
                .append("createdDT", createdDT)
                .append("activatedDT", activatedDT)
                .append("notifiedDT", notifiedDT)
                .append("accounts", accounts)
                .toString();
    }

    // interface for JPA

    protected User() {
    }

}
