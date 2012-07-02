package com.redwerk.likelabs.application.dto.statistics;

import java.util.Date;


public class ChartPoint {

    private Date date;

    private int photosTaken;

    private int facebook;

    private int vkontakte;

    private int emails;

    public ChartPoint() {
    }
    
    public ChartPoint(Date date, int photosTaken, int facebook, int vkontakte, int emails) {
        this.date = date;
        this.photosTaken = photosTaken;
        this.facebook = facebook;
        this.vkontakte = vkontakte;
        this.emails = emails;
    }
    
    public void addPoint(ChartPoint point) {
        this.photosTaken += point.photosTaken;
        this.facebook += point.facebook;
        this.vkontakte += point.vkontakte;
        this.emails += point.emails;
    }
    
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getEmails() {
        return emails;
    }

    public void setEmails(int emails) {
        this.emails = emails;
    }

    public int getFacebook() {
        return facebook;
    }

    public void setFacebook(int facebook) {
        this.facebook = facebook;
    }

    public int getPhotosTaken() {
        return photosTaken;
    }

    public void setPhotosTaken(int photosTaken) {
        this.photosTaken = photosTaken;
    }

    public int getVkontakte() {
        return vkontakte;
    }

    public void setVkontakte(int vkontakte) {
        this.vkontakte = vkontakte;
    }

}
