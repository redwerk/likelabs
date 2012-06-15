package com.redwerk.likelabs.web.ui.security;

import com.redwerk.likelabs.domain.model.user.User;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Authenticator {

    public static final Byte ANONYMOUS_USER_ID = 0;

    void authenticateUser(HttpServletRequest request, User user);

    void logoutUser(HttpServletRequest request, HttpServletResponse response);

    void authenticateAnonymous(HttpServletRequest request);

    Long getCurrentUserId();
}
