package com.redwerk.likelabs.infrastructure.security;

import com.redwerk.likelabs.application.CompanyService;
import com.redwerk.likelabs.domain.model.query.Pager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class AbstractUserDetailsService {

    @Autowired
    private CompanyService companyService;

    protected UserDetails createDetails(com.redwerk.likelabs.domain.model.user.User user) throws UsernameNotFoundException {

        if (user == null) {
            throw new UsernameNotFoundException("User not found.");
        }
        if (user.isSystemAdmin()) {
            return new User(user.getId().toString(), user.getPassword(),
                                  AuthorityUtils.createAuthorityList(AuthorityRole.ROLE_SYSTEM_ADMIN.toString()));
        }
        if (!companyService.getCompaniesForAdmin(user.getId(), Pager.ALL_RECORDS).getItems().isEmpty() && user.isActive()) {
            return new User(user.getId().toString(), user.getPassword(),
                                  AuthorityUtils.createAuthorityList(AuthorityRole.ROLE_COMPANY_ADMIN.toString()));
        }
        return new User(user.getId().toString(), user.getPassword(),
                                  AuthorityUtils.createAuthorityList(AuthorityRole.ROLE_USER.toString()));
    }
}
