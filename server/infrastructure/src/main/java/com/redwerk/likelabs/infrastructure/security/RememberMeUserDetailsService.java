package com.redwerk.likelabs.infrastructure.security;

import com.redwerk.likelabs.domain.model.user.User;
import com.redwerk.likelabs.domain.model.user.exception.UserNotFoundException;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

@Component(value="rememberMeUserDetailsService")
public class RememberMeUserDetailsService extends AbstractUserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException, DataAccessException {
        
        User user;
        try {
            user = userService.getUser(Long.parseLong(userId));
        } catch (UserNotFoundException e) {
            log.error(e,e);
            throw new UsernameNotFoundException("User not found.");
        }
        return createDetails(user);
    }
}
