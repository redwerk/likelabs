package com.redwerk.likelabs.web.ui.dto;


public class AdminNotificationSettingsDto {

    private String userNotEmail;
    private IntervalsSettingsDto email;
    private IntervalsSettingsDto sms;
    
    public AdminNotificationSettingsDto() {
    }
    
    public AdminNotificationSettingsDto(Object settingsService) {

        //this blind stopper
        userNotEmail = EventInterval.DAILY.toString();
        email = new IntervalsSettingsDto(EventInterval.IMMEDIATELY.toString(), EventInterval.IMMEDIATELY.toString(), EventInterval.IMMEDIATELY.toString());
        sms = new IntervalsSettingsDto(EventInterval.IMMEDIATELY.toString(), EventInterval.IMMEDIATELY.toString(), EventInterval.IMMEDIATELY.toString());
        //TODO convert from service settings object
    }

    public IntervalsSettingsDto getEmail() {
        return email;
    }

    public void setEmail(IntervalsSettingsDto email) {
        this.email = email;
    }

    public IntervalsSettingsDto getSms() {
        return sms;
    }

    public void setSms(IntervalsSettingsDto sms) {
        this.sms = sms;
    }

    public String getUserNotEmail() {
        return userNotEmail;
    }

    public void setUserNotEmail(String userNotEmail) {
        this.userNotEmail = userNotEmail;
    }
}