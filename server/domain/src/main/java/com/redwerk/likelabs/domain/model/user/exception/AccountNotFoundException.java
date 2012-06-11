package com.redwerk.likelabs.domain.model.user.exception;

import com.redwerk.likelabs.domain.model.SocialNetworkType;

public class AccountNotFoundException extends RuntimeException {
    
    private final SocialNetworkType snType;
    
    private final String accountId;

    public AccountNotFoundException(SocialNetworkType snType, String accountId) {
        this.snType = snType;
        this.accountId = accountId;
    }

    public SocialNetworkType getSnType() {
        return snType;
    }

    public String getAccountId() {
        return accountId;
    }

}

