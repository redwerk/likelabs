package com.redwerk.likelabs.domain.model.settings;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.persistence.*;

@Entity
@Table(name = "system_parameter")
public class SystemParameter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private SystemParameterType type;

    private String value;


    // constructors

    public SystemParameter(SystemParameterType type, String value) {
        this.type = type;
        this.value = value;
    }

    // accessors

    public SystemParameterType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    // modifiers

    public void setValue(String value) {
        this.value = value;
    }

    // overrides

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SystemParameter other = (SystemParameter) obj;
        return new EqualsBuilder()
                .append(type, other.type)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(type)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("type", type)
                .append("value", value)
                .toString();
    }

    // interface for JPA

    protected SystemParameter() {
    }

}
