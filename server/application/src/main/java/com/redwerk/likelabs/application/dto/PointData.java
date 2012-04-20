package com.redwerk.likelabs.application.dto;

public class PointData {
    
    private final String phone;

    private final TabletCredentialsData tabletCredentials;

    public PointData(String phone, TabletCredentialsData tabletCredentials) {
        this.phone = phone;
        this.tabletCredentials = tabletCredentials;
    }

    public String getPhone() {
        return phone;
    }

    public TabletCredentialsData getTabletCredentials() {
        return tabletCredentials;
    }

}
