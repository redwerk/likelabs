package com.redwerk.likelabs.web.rest.dto.exposable;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name="references")
public class ReferenceData {
    
    @XmlElementWrapper(name="links")
    @XmlElement(name="link")
    private List<LinkElement> links;
    

    public ReferenceData(List<LinkElement> links) {
        this.links = links;
    }
    
    /** Used internally by JAXB for object instantiation */
    @SuppressWarnings("unused")
    private ReferenceData() {
        this(null);
    }

    public List<LinkElement> getLinks() {
        return links;
    }
}
