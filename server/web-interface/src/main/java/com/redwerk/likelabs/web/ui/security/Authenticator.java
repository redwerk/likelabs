package com.redwerk.likelabs.web.ui.security;

import com.redwerk.likelabs.domain.model.user.User;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Authenticator {

    void authenticateUser(HttpServletRequest request, User user);

    void logoutUser(HttpServletRequest request, HttpServletResponse response);

    void authenticateAnonymous(HttpServletRequest request);

}
