package com.redwerk.likelabs.web.rest.dto.exposable;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

public class LinkElement {
    
    @XmlAttribute(required=false) 
    public final String ref;
      
    @XmlValue 
    public final String link;


    
    /** Used internally by JAXB for object instantiation */
    @SuppressWarnings("unused")
    private LinkElement() {
        this(null, null);
    }
    
    public LinkElement(String link) {
        this(link, null);
    }
    
    public LinkElement(String link, String ref) {
        this.link = link;
        this.ref = ref;
    }
}
