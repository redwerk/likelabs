package com.redwerk.likelabs.web.ui.security;


public interface DecisionAccess {

    boolean permissionCompany(Object principal, Long companyId);

    boolean permissionUser(Object principal, Long userId);

    boolean permissionCompanyAdmin(Object principal, Long userId);

    boolean permissionAdmin(Object principal);
}
