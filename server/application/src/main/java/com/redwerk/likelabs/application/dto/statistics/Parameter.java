package com.redwerk.likelabs.application.dto.statistics;

public class Parameter {
    
    private final ParameterType type;

    private final Totals totals;

    public Parameter(ParameterType type, Totals totals) {
        this.type = type;
        this.totals = totals;
    }

    public ParameterType getType() {
        return type;
    }

    public Totals getTotals() {
        return totals;
    }

}
