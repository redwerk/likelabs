package com.redwerk.likelabs.web.ui.security.impl;

import com.redwerk.likelabs.infrastructure.security.AuthorityRole;
import com.redwerk.likelabs.infrastructure.security.CustomUserDetails;
import com.redwerk.likelabs.web.ui.security.DecisionAccess;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component(value="decisionAccess")
public class DecisionAccessImpl implements DecisionAccess {

    @Override
    public boolean permissionCompany(Object principal, Long companyId) {

        if (!(principal instanceof CustomUserDetails)) {
            return false;
        }
        CustomUserDetails user  = (CustomUserDetails)principal;
        Collection<GrantedAuthority> authorities = user.getAuthorities();
        if (authorities.contains(new SimpleGrantedAuthority(AuthorityRole.ROLE_SYSTEM_ADMIN.toString()))) {
            return true;
        }
        if (user.getCompanyIds().contains(companyId) && authorities.contains(new SimpleGrantedAuthority(AuthorityRole.ROLE_COMPANY_ADMIN.toString()))) {
            return true;
        }
        return false;
    }

    @Override
    public boolean permissionUser(Object principal, Long userId) {
        if (!(principal instanceof CustomUserDetails)) {
            return false;
        }
        CustomUserDetails user  = (CustomUserDetails)principal;
        Collection<GrantedAuthority> authorities = user.getAuthorities();
        if (authorities.contains(new SimpleGrantedAuthority(AuthorityRole.ROLE_SYSTEM_ADMIN.toString()))) {
            return true;
        }
        if (user.getId().equals(userId) && authorities.contains(new SimpleGrantedAuthority(AuthorityRole.ROLE_USER.toString()))) {
            return true;
        }
        return false;
    }

    @Override
    public boolean permissionCompanyAdmin(Object principal, Long userId) {
        if (!(principal instanceof CustomUserDetails)) {
            return false;
        }
        CustomUserDetails user  = (CustomUserDetails)principal;
        Collection<GrantedAuthority> authorities = user.getAuthorities();
        if (authorities.contains(new SimpleGrantedAuthority(AuthorityRole.ROLE_SYSTEM_ADMIN.toString()))) {
            return true;
        }
        if (user.getId().equals(userId) && authorities.contains(new SimpleGrantedAuthority(AuthorityRole.ROLE_COMPANY_ADMIN.toString()))) {
            return true;
        }
        return false;
    }

    @Override
    public boolean permissionAdmin(Object principal) {
        if (!(principal instanceof CustomUserDetails)) {
            return false;
        }
        CustomUserDetails user  = (CustomUserDetails)principal;
        Collection<GrantedAuthority> authorities = user.getAuthorities();
        if (authorities.contains(new SimpleGrantedAuthority(AuthorityRole.ROLE_SYSTEM_ADMIN.toString()))) {
            return true;
        }
        return false;
    }
}
