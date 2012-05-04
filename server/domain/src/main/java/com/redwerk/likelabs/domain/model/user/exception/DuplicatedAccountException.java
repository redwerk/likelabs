package com.redwerk.likelabs.domain.model.user.exception;

import com.redwerk.likelabs.domain.model.SocialNetworkType;
import com.redwerk.likelabs.domain.model.user.User;

public class DuplicatedAccountException extends UserAccountException {

    public DuplicatedAccountException(User user, SocialNetworkType accountType) {
        super(user, accountType);
    }

}
