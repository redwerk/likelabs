package com.redwerk.likelabs.web.rest.dto.exposable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.redwerk.likelabs.domain.model.review.RecipientType;
import com.redwerk.likelabs.domain.service.dto.RecipientData;

@XmlAccessorType(XmlAccessType.FIELD)
public class RecipientDataExposable {

    private final RecipientType type;

    private final String address;


    public RecipientDataExposable(RecipientType type, String address) {
        this.type =  type;
        this.address = address;
    }

    public RecipientDataExposable(RecipientData recipientData) {
        this.type =  recipientData.getType();
        this.address = recipientData.getAddress();
    }   

    /** Used internally by JAXB for object instantiation */
    @SuppressWarnings("unused")
    private RecipientDataExposable () {
        this(null, null);        
    }


    public RecipientType getType() {
        return type;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
        .append("type", type)
        .append("address", address)
        .toString();
    }
}
