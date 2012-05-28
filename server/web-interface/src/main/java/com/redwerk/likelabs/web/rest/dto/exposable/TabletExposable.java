package com.redwerk.likelabs.web.rest.dto.exposable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.redwerk.likelabs.domain.model.tablet.Tablet;

@XmlRootElement(name = "tablet")
@XmlAccessorType(XmlAccessType.FIELD)
public class TabletExposable {
	
	private final long id;
	
	private final String apiKey;

	private TabletSettings settings;
 

	public TabletExposable(Long id, String apiKey) {
		this.id = id;
		this.apiKey = apiKey;
	}
	
	public TabletExposable(Tablet tablet) {		
		this(tablet.getId(), tablet.getApiKey());
	}
	
	/** Used internally by JAXB for object instantiation */
	@SuppressWarnings("unused")
    private TabletExposable() {
		this(null, null);
	}
	
	public long getId() {
		return id;
	}

	public String getApiKey() {
		return apiKey;
	}

    
    public TabletSettings getSettings() {
        return settings;
    }

    
    public void setSettings(TabletSettings settings) {
        this.settings = settings;
    }
}
