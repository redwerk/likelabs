package com.redwerk.likelabs.domain.model.user;

import com.redwerk.likelabs.domain.model.SocialAccount;
import com.redwerk.likelabs.domain.model.SocialAccountType;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
public class UserSocialAccount implements SocialAccount, Comparable<UserSocialAccount> {

    @Enumerated(value = EnumType.ORDINAL)
    private SocialAccountType type;

    private String name;


    // constructors

    public UserSocialAccount(SocialAccountType type, String name) {
        this.type = type;
        this.name = name;
    }

    // implementation of SocialAccount

    @Override
    public SocialAccountType getType() {
        return type;
    }

    @Override
    public String getName() {
        return name;
    }

    // implementation of Comparable<UserSocialAccount>

    @Override
    public int compareTo(UserSocialAccount other) {
        int res = type.compareTo(other.type);
        return (res != 0) ? res: name.compareTo(other.name);
    }

    // overrides

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        UserSocialAccount other = (UserSocialAccount) obj;
        return new EqualsBuilder()
                .append(type, other.type)
                .append(name, other.name)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(type)
                .append(name)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("type", type)
                .append("name", name)
                .toString();
    }

}
