package com.redwerk.likelabs.application.dto.statistics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Statistics {
    
    private final List<Parameter> parameters;

    public Statistics(List<Parameter> parameters) {
        this.parameters = new ArrayList<Parameter> (parameters);
    }

    public List<Parameter> getParameters() {
        return Collections.unmodifiableList(parameters);
    }

}
