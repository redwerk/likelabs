package com.redwerk.likelabs.infrastructure.security;

import com.redwerk.likelabs.domain.model.user.User;
import com.redwerk.likelabs.domain.model.user.exception.UserNotFoundException;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component(value="loginUserDetailsService")
public class LoginUserDetailsService extends AbstractUserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException, DataAccessException {

        User user;
        try {
            user = userService.findUser(phone);
        } catch (UserNotFoundException e) {
            log.error(e,e);
            throw new UsernameNotFoundException("User not found.");
        }
        return createDetails(user);
    }
}
