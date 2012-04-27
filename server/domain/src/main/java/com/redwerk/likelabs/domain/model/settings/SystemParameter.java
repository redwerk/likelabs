package com.redwerk.likelabs.domain.model.settings;

public class SystemParameter {
    
    private Long id;

    private SystemParameterType type;

    private String value;

    public SystemParameter(SystemParameterType type, String value) {
        this.type = type;
        this.value = value;
    }

    public SystemParameterType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
