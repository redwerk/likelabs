package com.redwerk.likelabs.domain.service.sn.exception;

import com.redwerk.likelabs.domain.model.company.CompanySocialPage;
import com.redwerk.likelabs.domain.model.user.UserSocialAccount;

public class ResourceAccessDeniedException extends SNException {

    private final SNResourceType resourceType;

    private final UserSocialAccount userAccount;

    private final CompanySocialPage companyPage;

    public ResourceAccessDeniedException(SNResourceType resourceType, UserSocialAccount userAccount,
                                         CompanySocialPage companyPage) {
        this.resourceType = resourceType;
        this.userAccount = userAccount;
        this.companyPage = companyPage;
    }

    public ResourceAccessDeniedException(SNResourceType resourceType, UserSocialAccount userAccount) {
        this(resourceType, userAccount, null);
    }

    public SNResourceType getResourceType() {
        return resourceType;
    }

    public UserSocialAccount getUserAccount() {
        return userAccount;
    }

    public CompanySocialPage getCompanyPage() {
        return companyPage;
    }

}
