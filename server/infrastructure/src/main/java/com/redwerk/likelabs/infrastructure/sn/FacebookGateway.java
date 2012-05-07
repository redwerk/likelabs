package com.redwerk.likelabs.infrastructure.sn;

import com.redwerk.likelabs.application.sn.SocialNetworkGateway;
import com.redwerk.likelabs.domain.model.company.CompanySocialPage;
import com.redwerk.likelabs.domain.model.user.UserSocialAccount;
import org.springframework.stereotype.Service;

@Service
public class FacebookGateway implements SocialNetworkGateway {

    @Override
    public UserSocialAccount getUserAccount(String code) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public CompanySocialPage getCompanyPage(String pageUrl) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getUserEmail(UserSocialAccount account) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void postUserMessage(UserSocialAccount publisher, String message) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void postCompanyMessage(CompanySocialPage page, UserSocialAccount publisher, String message) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isAdminFor(UserSocialAccount account, CompanySocialPage page) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

}
