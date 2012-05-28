package com.redwerk.likelabs.web.rest.dto.exposable;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

public class MapElement {
    
    @XmlAttribute 
    public String key;
    
    @XmlValue 
    public String value;
    
    /** Used internally by JAXB for object instantiation */
    @SuppressWarnings("unused")
    private MapElement() {
    }

    public MapElement(String key, String value) {
        this.key = key;
        this.value = value;
    }
}
