package com.redwerk.likelabs.application.dto;

public class TabletData {

    private final String login;

    private final String loginPassword;

    private final String logoutPassword;

    public TabletData(String login, String loginPassword, String logoutPassword) {
        this.login = login;
        this.loginPassword = loginPassword;
        this.logoutPassword = logoutPassword;
    }

    public String getLogin() {
        return login;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public String getLogoutPassword() {
        return logoutPassword;
    }
    
}
