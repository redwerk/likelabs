package com.redwerk.likelabs.web.rest.dto.exposable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlMimeType;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.redwerk.likelabs.domain.model.photo.PhotoStatus;
import com.redwerk.likelabs.domain.service.dto.PhotoData;

@XmlAccessorType(XmlAccessType.FIELD)
public class PhotoDataExposable {

    private final PhotoStatus status;

    @XmlMimeType(value="image/*")
    private final byte[] image;


    public PhotoDataExposable(PhotoStatus status, byte[] image) {
        this.status = status;
        this.image = image;
    }

    public PhotoDataExposable(PhotoData photoData) {
        this.status = photoData.getStatus();
        this.image = photoData.getImage();
    }

    /** Used internally by JAXB for object instantiation */
    @SuppressWarnings("unused")
    private PhotoDataExposable() {
        this(null, null);        
    }

    public byte[] getImage() {
        return image;
    }

    public PhotoStatus getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
        .append("status", status)
        .append("image data size", image==null ? "empty" : image.length)
        .toString();
    }
}