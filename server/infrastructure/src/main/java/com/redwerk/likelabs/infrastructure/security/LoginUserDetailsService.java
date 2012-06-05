package com.redwerk.likelabs.infrastructure.security;

import com.redwerk.likelabs.application.UserService;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(value="loginUserDetailsService")
public class LoginUserDetailsService extends AbstractUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException, DataAccessException {

        return createDetails(userService.findUser(phone));
    }
}
