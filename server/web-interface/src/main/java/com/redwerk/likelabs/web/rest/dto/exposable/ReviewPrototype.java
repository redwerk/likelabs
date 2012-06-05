package com.redwerk.likelabs.web.rest.dto.exposable;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.builder.ToStringBuilder;

@XmlRootElement(name="review")
@XmlAccessorType(XmlAccessType.FIELD)
public class ReviewPrototype {

    private String authorPhone;

    private String text;

    @XmlElementWrapper(name="photos")
    @XmlElement(name="photo")
    private List<PhotoDataExposable> photos;

    @XmlElementWrapper(name="recipients")
    @XmlElement(name="recipient")
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

    public List<RecipientDataExposable> getRecipients() {
        return recipients;
    }

    public void setAuthorPhone(String authorPhone) {
        this.authorPhone = authorPhone;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setPhotos(List<PhotoDataExposable> photos) {
        this.photos = photos;
    }

    public void setRecipients(List<RecipientDataExposable> recipients) {
        this.recipients = recipients;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
        .append("authorPhone", authorPhone)
        .append("text", text)
        .append("recipients", recipients)
        .append("photos", photos)
        .toString();
    }
}
