package com.redwerk.likelabs.domain.model.user.exception;

import com.redwerk.likelabs.domain.model.SocialNetworkType;
import com.redwerk.likelabs.domain.model.user.User;

public class AccountNotExistsException extends UserAccountException {

    public AccountNotExistsException(User user, SocialNetworkType accountType) {
        super(user, accountType);
    }

}
