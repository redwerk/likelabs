package com.redwerk.likelabs.application.impl.registration.exception;


public class NotConfirmMailException  extends RuntimeException {

    private final long userId;

    private final String email;

    private final String confirmationCode;


    public NotConfirmMailException(long userId, String email,String confirmationCode) {
        this.userId = userId;
        this.email = email;
        this.confirmationCode = confirmationCode;
    }

    public String getConfirmationCode() {
        return confirmationCode;
    }

    public String getEmail() {
        return email;
    }

    public long getUserId() {
        return userId;
    }

    @Override
    public String getMessage() {
        return "cofirm mail failed for user id:" + String.valueOf(userId) + " and e-mail:" + email + ", confirmation code:" +confirmationCode;
    }
}
