package com.redwerk.likelabs.web.ui.security.impl;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component(value="authFailureHandler")
public class SecurityAuthenticationFailureHandler implements AuthenticationFailureHandler{

    private final Logger log = LogManager.getLogger(getClass());

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        
        if  (exception.getCause() != null) {
            log.error(exception.getCause(),exception.getCause());
        }
        PrintWriter out = response.getWriter();
        out.write("false");
        out.close();
    }

}
