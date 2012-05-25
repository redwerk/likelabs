package com.redwerk.likelabs.domain.model.user;

import com.redwerk.likelabs.domain.model.SocialNetworkType;
import com.redwerk.likelabs.domain.model.review.Review;
import com.redwerk.likelabs.domain.service.sn.GatewayFactory;
import com.redwerk.likelabs.domain.service.sn.SocialNetworkGateway;
import com.redwerk.likelabs.domain.service.sn.exception.SNGeneralException;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
public class UserSocialAccount implements Comparable<UserSocialAccount> {
    
    private static final Logger LOGGER = Logger.getLogger(UserSocialAccount.class);

    @Enumerated(value = EnumType.ORDINAL)
    private SocialNetworkType type;

    @Column(name = "account_id")
    private String accountId;

    @Column(name = "access_token")
    private String accessToken;

    private String name;


    // constructors

    public UserSocialAccount(SocialNetworkType type, String accountId, String accessToken, String name) {
        this.type = type;
        this.accountId = accountId;
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
    
    // reviews publishing
    
    public boolean publishReview(Review review, GatewayFactory gatewayFactory) {
        SocialNetworkGateway snGateway = gatewayFactory.getGateway(type);
        try {
            snGateway.postUserMessage(this, review.getMessage(), review.getPhoto().getImage());
        }
        catch (SNGeneralException e) {
            LOGGER.error("cannot publish review in " + type, e);
            return false;
        }
        return true;
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
