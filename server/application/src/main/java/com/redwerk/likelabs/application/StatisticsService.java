package com.redwerk.likelabs.application;

import com.redwerk.likelabs.application.dto.statistics.Interval;
import com.redwerk.likelabs.application.dto.statistics.PointsStatistics;
import com.redwerk.likelabs.application.dto.statistics.TotalsStatistics;
import com.redwerk.likelabs.application.dto.statistics.StatisticsType;

import java.util.List;

public interface StatisticsService {
    
    TotalsStatistics getTotals(long companyId, StatisticsType type);

    List<PointsStatistics> getPoints(long companyId, Interval interval);

}
