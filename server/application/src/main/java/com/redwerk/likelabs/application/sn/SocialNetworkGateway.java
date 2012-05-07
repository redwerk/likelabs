package com.redwerk.likelabs.application.sn;

import com.redwerk.likelabs.domain.model.company.CompanySocialPage;
import com.redwerk.likelabs.domain.model.user.UserSocialAccount;

public interface SocialNetworkGateway {

    /**
     * Creates UserSocialAccount object by code for access token
     * @param code for access token retrieving
     * @return UserSocialAccount object representing user account in social network
     */
    public UserSocialAccount getUserAccount(String code);

    /**
     * Validates that passed URL is company page URL in social network and creates CompanySocialPage object
     * @param pageUrl URL of company page in social network
     * @return CompanySocialPage object representing company page in social network
     */
    public CompanySocialPage getCompanyPage(String pageUrl);

    /**
     * Obtains email address for the user account in social network
     * @param account user account in the social network
     * @return user email address
     */
    public String getUserEmail(UserSocialAccount account);

    /**
     * Post message to the user page in the social network
     * @param publisher publisher account in the social account
     * @param message text of the message
     */
    public void postUserMessage(UserSocialAccount publisher, String message);

    /**
     * Post message to the company page in the social network
     * @param page company page in the social network
     * @param publisher publisher in the social account
     * @param message text of the message
     */
    public void postCompanyMessage(CompanySocialPage page, UserSocialAccount publisher, String message);

    /**
     * Checks if user account has administrative privileges for the company page in social network
     * @param account user account in social network
     * @param page company page in social network
     * @return true if user is admin for the company, false - otherwise
     */
    public boolean isAdminFor(UserSocialAccount account, CompanySocialPage page);

}
