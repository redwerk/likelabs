package com.redwerk.likelabs.web.rest.dto.exposable;

import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.redwerk.likelabs.web.rest.dto.exposable.adapter.MapAdapter;

@XmlRootElement(name="references")
public class ReferenceData {
    
    @XmlJavaTypeAdapter(MapAdapter.class)
    private final Map<String, String> links;

    
    public ReferenceData(Map<String, String> links) {
        this.links = links;
    }
    
    /** Used internally by JAXB for object instantiation */
    @SuppressWarnings("unused")
    private ReferenceData() {
        this(null);
    }

    public Map<String, String> getLinks() {
        return links;
    }
}
