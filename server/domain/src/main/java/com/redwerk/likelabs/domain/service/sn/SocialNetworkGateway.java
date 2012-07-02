package com.redwerk.likelabs.domain.service.sn;

import com.redwerk.likelabs.domain.model.company.Company;
import com.redwerk.likelabs.domain.model.company.CompanySocialPage;
import com.redwerk.likelabs.domain.model.user.UserSocialAccount;

public interface SocialNetworkGateway {

    /**
     * Creates UserSocialAccount object by code for access token
     * @param code for access token retrieving
     * @return UserSocialAccount object representing user account in social network
     */
    public UserSocialAccount getUserAccountByCode(String code);

    /**
     * Creates UserSocialAccount object by access token
     * @param accessToken for access to User Social Page
     * @return UserSocialAccount object representing user account in social network
     */
    public UserSocialAccount getUserAccountByAccessToken(String accessToken);

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
     * @param imageSource helper object for retrieving image in appropriate form (url or array of bytes)
     */
    public String postUserMessage(UserSocialAccount publisher, String message, ImageSource imageSource);

    /**
     * Post message to the company page in the social network
     * @param page company page in the social network
     * @param publisher publisher in the social account
     * @param message text of the message
     * @param imageSource helper object for retrieving image in appropriate form (url or array of bytes)
     */
    public String postCompanyMessage(CompanySocialPage page, UserSocialAccount publisher, String message, ImageSource imageSource);

    /**
     * Post message to the company page in the social network
     * @param page company page in the social network
     * @param company publisher company
     * @param message text of the message
     * @param imageSource helper object for retrieving image in appropriate form (url or array of bytes)
     */
    public String postCompanyMessage(CompanySocialPage page, Company company, String message, ImageSource imageSource);

    /**
     * Checks if user account has administrative privileges for the company page in social network
     * @param account user account in social network
     * @param page company page in social network
     * @return true if user is admin for the company, false - otherwise
     */
    public boolean isAdminFor(UserSocialAccount account, CompanySocialPage page);
    
    
    
    public Object getStatisticsCompany(CompanySocialPage page, UserSocialAccount account);

    public Object getStatisticsUser(UserSocialAccount account);
}
