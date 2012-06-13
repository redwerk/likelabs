package com.redwerk.likelabs.web.ui.security.impl;

import com.redwerk.likelabs.domain.model.user.User;
import com.redwerk.likelabs.infrastructure.security.AuthorityRole;
import com.redwerk.likelabs.infrastructure.security.CustomUserDetails;
import com.redwerk.likelabs.web.ui.security.Authenticator;
import java.util.Collection;
import java.util.HashSet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

@Component(value="authenticator")
public class SimpleAuthenticator implements Authenticator{

    public static final Byte ANONYMOUS_USER_ID = 0;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RememberMeServices rememberMeServices;

    @Override
    public void authenticateUser(HttpServletRequest request, User user) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getPhone(), user.getPassword());
        WebAuthenticationDetails details = new WebAuthenticationDetails(request);
        authenticationToken.setDetails(details);
        Authentication fullauth = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(fullauth);
    }

    @Override
    public void logoutUser(HttpServletRequest request, HttpServletResponse response) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
            rememberMeServices.loginFail(request, response);
        }
    }

    @Override
    public void authenticateAnonymous(HttpServletRequest request) {
        
        Collection<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority(AuthorityRole.ROLE_ANONYMOUS.toString()));
        AnonymousAuthenticationToken authenticationToken = new AnonymousAuthenticationToken("LikeLabsWebSiteKey", "0", authorities);
        WebAuthenticationDetails details = new WebAuthenticationDetails(request);
        authenticationToken.setDetails(details);
        Authentication fullauth = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(fullauth);
    }

    @Override
    public Long getCurrentUserId() {

        Authentication auth= SecurityContextHolder.getContext().getAuthentication();
        if (auth.getPrincipal() instanceof CustomUserDetails) {
            return ((CustomUserDetails)auth.getPrincipal()).getId();
        }
        return ANONYMOUS_USER_ID.longValue();
    }
}
