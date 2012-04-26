package com.redwerk.likelabs.domain.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;


public interface SocialAccount {

    SocialAccountType getType();

    String getName();

}
