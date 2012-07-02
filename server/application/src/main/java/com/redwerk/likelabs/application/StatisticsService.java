package com.redwerk.likelabs.application;

import com.redwerk.likelabs.application.dto.statistics.ChartPoint;
import com.redwerk.likelabs.application.dto.statistics.Interval;
import com.redwerk.likelabs.application.dto.statistics.StatisticsType;
import com.redwerk.likelabs.application.dto.statistics.TotalsStatistics;
import java.util.List;


public interface StatisticsService {
    
    TotalsStatistics getStatistics(long companyId, StatisticsType statisticsType);

    List<ChartPoint> getChartPoints(long companyId, Interval interval);

}
