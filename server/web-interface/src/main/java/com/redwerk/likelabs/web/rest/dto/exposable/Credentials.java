package com.redwerk.likelabs.web.rest.dto.exposable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="credentials")
@XmlAccessorType(XmlAccessType.FIELD)
public class Credentials {
    
    private final String login;
    
    private final String password;
    
    
    public Credentials(String login, String password) {
    	this.login = login;
    	this.password = password;
    }
    
    /** Used internally by JAXB for object instantiation */
    @SuppressWarnings("unused")
    private Credentials() {
    	this(null, null);
    }

	public String getLogin() {
		return login;
	}

	public String getPassword() {
		return password;
	}

}
