package com.redwerk.likelabs.application;

public interface SocialNetworkInterface {

    public String getUserName() throws Exception;

    public String getEmail() throws Exception;
    
    public String publishMessage(String message) throws Exception;
    
    public boolean checkIsAdmin(String companyId) throws Exception;
}
