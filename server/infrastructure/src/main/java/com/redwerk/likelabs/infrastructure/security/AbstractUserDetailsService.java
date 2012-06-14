package com.redwerk.likelabs.infrastructure.security;

import com.redwerk.likelabs.application.CompanyService;
import com.redwerk.likelabs.application.UserService;
import com.redwerk.likelabs.application.dto.Report;
import com.redwerk.likelabs.application.dto.company.CompanyReportItem;
import com.redwerk.likelabs.domain.model.query.Pager;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public abstract class AbstractUserDetailsService implements UserDetailsService {

    @Autowired
    protected  UserService userService;

    @Autowired
    private CompanyService companyService;

    protected final Logger log = LogManager.getLogger(getClass());

    protected UserDetails createDetails(com.redwerk.likelabs.domain.model.user.User user) throws UsernameNotFoundException {

        if (user == null) {
            throw new UsernameNotFoundException("User not found.");
        }
        if (user.isSystemAdmin()) {
            return new CustomUserDetails(user.getId(), user.getPhone(), user.getPassword(),
                                  AuthorityUtils.createAuthorityList(AuthorityRole.ROLE_SYSTEM_ADMIN.toString()),null);
        }
        Report<CompanyReportItem> report = companyService.getCompaniesForAdmin(user.getId(), Pager.ALL_RECORDS);
        if (report.getItems().isEmpty()) {
            return new CustomUserDetails(user.getId(), user.getPhone(), user.getPassword(),
                                  AuthorityUtils.createAuthorityList(AuthorityRole.ROLE_USER.toString()), null);
        }
        Set<Long> companyIds = new HashSet<Long>();
        for (CompanyReportItem item: report.getItems()) {
            companyIds.add(item.getCompany().getId());
        }
        if (!user.isActive()) {
            throw new UsernameNotFoundException("Company administrator not activated.");
        }
        return new CustomUserDetails(user.getId(), user.getPhone(), user.getPassword(),
                                  AuthorityUtils.createAuthorityList(AuthorityRole.ROLE_COMPANY_ADMIN.toString()), companyIds);
    }
}
