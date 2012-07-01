package com.redwerk.likelabs.application.dto.statistics;

import java.util.Date;


public class ChartPoint {

    private Date date;
    private Integer photosTaken;
    private Integer facebook;
    private Integer vkontakte;
    private Integer emails;

    public ChartPoint() {    }
    
    public ChartPoint(Date date, Integer photosTaken, Integer facebook, Integer vkontakte, Integer emails){
        this.date = date;
        this.photosTaken = photosTaken;
        this.facebook = facebook;
        this.vkontakte = vkontakte;
        this.emails = emails;
    }
    
    public void addPoint(ChartPoint point){
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

    public Integer getEmails() {
        return emails;
    }

    public void setEmails(Integer emails) {
        this.emails = emails;
    }

    public Integer getFacebook() {
        return facebook;
    }

    public void setFacebook(Integer facebook) {
        this.facebook = facebook;
    }

    public Integer getPhotosTaken() {
        return photosTaken;
    }

    public void setPhotosTaken(Integer photosTaken) {
        this.photosTaken = photosTaken;
    }

    public Integer getVkontakte() {
        return vkontakte;
    }

    public void setVkontakte(Integer vkontakte) {
        this.vkontakte = vkontakte;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ChartPoint other = (ChartPoint) obj;
        if (this.date != other.date && (this.date == null || !this.date.equals(other.date))) {
            return false;
        }
        if (this.photosTaken != other.photosTaken && (this.photosTaken == null || !this.photosTaken.equals(other.photosTaken))) {
            return false;
        }
        if (this.facebook != other.facebook && (this.facebook == null || !this.facebook.equals(other.facebook))) {
            return false;
        }
        if (this.vkontakte != other.vkontakte && (this.vkontakte == null || !this.vkontakte.equals(other.vkontakte))) {
            return false;
        }
        if (this.emails != other.emails && (this.emails == null || !this.emails.equals(other.emails))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (this.date != null ? this.date.hashCode() : 0);
        hash = 79 * hash + (this.photosTaken != null ? this.photosTaken.hashCode() : 0);
        hash = 79 * hash + (this.facebook != null ? this.facebook.hashCode() : 0);
        hash = 79 * hash + (this.vkontakte != null ? this.vkontakte.hashCode() : 0);
        hash = 79 * hash + (this.emails != null ? this.emails.hashCode() : 0);
        return hash;
    }
}
