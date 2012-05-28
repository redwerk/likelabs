package com.redwerk.likelabs.web.rest.dto.exposable;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name="review")
public class ReviewExposable {
    
    private String authorPhone;
    
    private String text;
    
    private List<PhotoDataExposable> photos;
    
    private List<RecipientDataExposable> recipients;


    public String getText() {
        return text;
    }
    
    public String getAuthorPhone() {
        return authorPhone;
    }   
    
    public List<PhotoDataExposable> getPhotos() {
        return photos;
    }
    
    @XmlElementWrapper(name="recipients")
    @XmlElement(name="recipient")
    public List<RecipientDataExposable> getRecipients() {
        return recipients;
    }
    
    public void setAuthorPhone(String authorPhone) {
        this.authorPhone = authorPhone;
    }

    public void setText(String text) {
        this.text = text;
    }
    
    @XmlElementWrapper(name="photos")
    @XmlElement(name="photo")   
    public void setPhotos(List<PhotoDataExposable> photos) {
        this.photos = photos;
    }
    
    public void setRecipients(List<RecipientDataExposable> recipients) {
        this.recipients = recipients;
    }
}