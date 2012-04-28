package com.redwerk.likelabs.domain.model.user;

import com.redwerk.likelabs.domain.model.SocialNetworkType;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
public class UserSocialAccount implements Comparable<UserSocialAccount> {

    @Enumerated(value = EnumType.ORDINAL)
    private SocialNetworkType type;

    private String accountId;
    
    private String accessToken;

    private String name;


    // constructors

    public UserSocialAccount(SocialNetworkType type, String accountId, String accessToken, String name) {
        this.type = type;
        this.accountId =accountId;
        this.accessToken = accessToken;
        this.name = name;
    }

    // accessors

    public SocialNetworkType getType() {
        return type;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getName() {
        return name;
    }

    // modifiers

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    // implementation of Comparable<UserSocialAccount>

    @Override
    public int compareTo(UserSocialAccount other) {
        int res = type.compareTo(other.type);
        return (res != 0) ? res: accountId.compareTo(other.accountId);
    }

    // overrides

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        UserSocialAccount other = (UserSocialAccount) obj;
        return new EqualsBuilder()
                .append(type, other.type)
                .append(accountId, other.accountId)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(type)
                .append(accountId)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("type", type)
                .append("accountId", accountId)
                .append("accessToken", accessToken)
                .append("name", name)
                .toString();
    }

    // interface for JPA

    protected UserSocialAccount() {
    }

}
