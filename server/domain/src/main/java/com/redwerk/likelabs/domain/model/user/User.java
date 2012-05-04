package com.redwerk.likelabs.domain.model.user;

import com.redwerk.likelabs.domain.model.SocialNetworkType;
import com.redwerk.likelabs.domain.model.user.exception.AccountNotExistsException;
import com.redwerk.likelabs.domain.model.user.exception.DuplicatedAccountException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;

import javax.persistence.*;
import java.util.*;

@Entity
public class User {
    
    private static final String ANONYMOUS_USER_NAME = "Anonymous";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String phone;

    private String password;

    @Column(name = "is_active")
    private boolean active;
    
    private String email;

    private boolean systemAdmin = false;

    @Column(name = "publish_in_sn")
    private boolean publishInSN = true;

    private boolean notifyIfClient = true;

    @Column(name = "created_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDT = new Date();

    @Column(name = "notified_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date notifiedDT;

    @ElementCollection
    @CollectionTable(name="user_social_account", joinColumns = @JoinColumn(name="user_id"))
    @Sort(type = SortType.NATURAL)
    private SortedSet<UserSocialAccount> accounts = new TreeSet<UserSocialAccount>();


    // constructors

    protected User(String phone, String password, boolean active) {
        this.phone = phone;
        this.password = password;
        this.active = active;
    }

    // accessors
    
    public boolean isActive() {
        return active;
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

    // modifiers

    public void activate() {
        if (active) {
            throw new IllegalStateException("user is already active");
        }
        active = true;
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

    public void setNotifyIfClient(boolean notifyIfClient) {
        this.notifyIfClient = notifyIfClient;
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

    public UserSocialAccount getPrimaryAccount() {
        return isAnonymous() ? null : accounts.first();
    }

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
                .append("active", active)
                .append("email", email)
                .append("systemAdmin", systemAdmin)
                .append("publishInSN", publishInSN)
                .append("notifyIfClient", notifyIfClient)
                .append("createdDT", createdDT)
                .append("notifiedDT", notifiedDT)
                .append("accounts", accounts)
                .toString();
    }

    // interface for JPA

    protected User() {
    }

}
