package com.redwerk.likelabs.application.dto;

public class TabletCredentialsData {

    private final String login;

    private final String password;

    public TabletCredentialsData(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

}
