package com.redwerk.likelabs.infrastructure.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class CustomUserDetails extends User {

    private Long id;
    private String phone;
    private Set<Long> companyIds;

    public CustomUserDetails(Long id, String phone, String password, Collection<? extends GrantedAuthority> authorities, Set<Long> companyIds) {
        this(id, phone, password, true, true, true, true, authorities, companyIds);
    }

    public CustomUserDetails(Long id, String phone, String password, boolean enabled, boolean accountNonExpired,
            boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities, Set<Long> companyIds) {

        super(String.valueOf(id), password, enabled, accountNonExpired,
            credentialsNonExpired, accountNonLocked, authorities);
        this.id = id;
        this.phone = phone;
        this.companyIds = (companyIds != null) ? companyIds : new HashSet<Long>();
    }

    public Set<Long> getCompanyIds() {
        return companyIds;
    }

    public Long getId() {
        return id;
    }

    public String getPhone() {
        return phone;
    }
}
