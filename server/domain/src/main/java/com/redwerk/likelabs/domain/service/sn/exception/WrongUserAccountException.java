package com.redwerk.likelabs.domain.service.sn.exception;

import com.redwerk.likelabs.domain.model.user.UserSocialAccount;

public class WrongUserAccountException extends SNGeneralException {

    private final UserSocialAccount userAccount;

    public WrongUserAccountException(UserSocialAccount userAccount) {
        this.userAccount = userAccount;
    }

    public UserSocialAccount getUserAccount() {
        return userAccount;
    }

}
