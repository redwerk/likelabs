package com.redwerk.likelabs.infrastructure.security;

//import org.jgeek.website.model.security.UserAccount;
import com.redwerk.likelabs.application.UserService;
import com.redwerk.likelabs.infrastructure.security.AuthorityRole;
import com.redwerk.likelabs.domain.model.user.User;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

//import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component(value="securityUserDetailsService")
@Transactional
public class SecurityUserDetailsService implements UserDetailsService {

    @Autowired
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException, DataAccessException {

        User user = userService.findUser(phone);
        if (user == null) {
            throw new UsernameNotFoundException(null);
        }
        return createDetails(user);
    }


    private UserDetails createDetails(User user) {

        Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority(AuthorityRole.ROLE_USER.toString()));
        if (user.isSystemAdmin()) {
            authorities.add(new SimpleGrantedAuthority(AuthorityRole.ROLE_SYSTEM_ADMIN.toString()));
        }
        return new org.springframework.security.core.userdetails.User(user.getPhone(), user.getPassword(), authorities);
   }
}
