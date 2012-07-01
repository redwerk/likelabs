package com.redwerk.likelabs.application;

import com.redwerk.likelabs.application.dto.statistics.ChartPoint;
import com.redwerk.likelabs.application.dto.statistics.Interval;
import com.redwerk.likelabs.application.dto.statistics.StatisticsType;
import com.redwerk.likelabs.application.dto.statistics.TotalsStatistics;
import java.util.List;
import java.util.Map;

public interface StatisticsService {
    
    Map<StatisticsType, TotalsStatistics> getAllStatistic(long companyId);

    List<ChartPoint> getChartPionts(long companyId, Interval interval);
}
