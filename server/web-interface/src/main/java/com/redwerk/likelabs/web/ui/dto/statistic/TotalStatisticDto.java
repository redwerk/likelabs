package com.redwerk.likelabs.web.ui.dto.statistic;

import com.redwerk.likelabs.application.dto.statistics.Parameter;
import com.redwerk.likelabs.application.dto.statistics.TotalsStatistics;
import com.redwerk.likelabs.application.template.MessageTemplateService;
import java.util.ArrayList;
import java.util.List;


public class TotalStatisticDto {

    private List<ItemTotalStatisticDto> items = new ArrayList<ItemTotalStatisticDto>();

    public TotalStatisticDto(TotalsStatistics total, MessageTemplateService messageTemplateService) {

        for (Parameter p : total.getParameters()) {
            items.add(new ItemTotalStatisticDto(p, messageTemplateService.getMessage("company.dashboard.statistic.type." + p.getType().toString())));
        }
    }

    public List<ItemTotalStatisticDto> getItems() {
        return items;
    }
}
