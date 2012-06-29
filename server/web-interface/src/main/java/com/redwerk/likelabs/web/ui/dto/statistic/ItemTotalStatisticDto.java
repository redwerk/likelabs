package com.redwerk.likelabs.web.ui.dto.statistic;

import com.redwerk.likelabs.application.dto.statistics.Parameter;
import com.redwerk.likelabs.application.dto.statistics.ParameterType;
import com.redwerk.likelabs.application.dto.statistics.Totals;


public class ItemTotalStatisticDto {

    private final ParameterType type;

    private final int all;

    private final int last24Hours;

    private final int lastWeek;

    private final int lastMonth;

    private final String description;

    public ItemTotalStatisticDto(Parameter param, String description) {
        this.type = param.getType();
        this.all = param.getTotals().getAll();
        this.last24Hours = param.getTotals().getLast24Hours();
        this.lastMonth = param.getTotals().getLastMonth();
        this.lastWeek = param.getTotals().getLastWeek();
        this.description = description;
    }

    public ParameterType getType() {
        return type;
    }

    public int getAll() {
        return all;
    }

    public int getLast24Hours() {
        return last24Hours;
    }

    public int getLastMonth() {
        return lastMonth;
    }

    public int getLastWeek() {
        return lastWeek;
    }

    public String getDescription() {
        return description;
    }
}
