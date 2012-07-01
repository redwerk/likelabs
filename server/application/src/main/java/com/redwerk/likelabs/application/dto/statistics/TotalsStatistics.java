package com.redwerk.likelabs.application.dto.statistics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TotalsStatistics {
    
    private final List<Parameter> parameters;

    public TotalsStatistics(List<Parameter> parameters) {
        this.parameters = new ArrayList<Parameter> (parameters);
    }

    public List<Parameter> getParameters() {
        return Collections.unmodifiableList(parameters);
    }
    
}
