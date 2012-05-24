package com.redwerk.likelabs.infrastructure.security;

//import org.jgeek.website.model.security.UserAccount;
import com.redwerk.likelabs.application.CompanyService;
import com.redwerk.likelabs.application.UserService;
import com.redwerk.likelabs.domain.model.query.Pager;
import com.redwerk.likelabs.infrastructure.security.AuthorityRole;
import com.redwerk.likelabs.domain.model.user.User;
import java.util.ArrayList;
import java.util.Collection;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

//import javax.inject.Inject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component(value="securityUserDetailsService")
@Transactional
public class SecurityUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Autowired
    private CompanyService CompanyService;

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
        if (CompanyService.getCompanies(user.getId(), Pager.ALL_RECORDS).getCount() > 0 && user.isActive()) {
            authorities.add(new SimpleGrantedAuthority(AuthorityRole.ROLE_COMPANY_ADMIN.toString()));
        }
        return new org.springframework.security.core.userdetails.User(user.getPhone(), user.getPassword(), authorities);
   }
}
