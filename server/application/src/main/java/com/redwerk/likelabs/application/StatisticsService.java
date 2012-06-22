package com.redwerk.likelabs.application;

import com.redwerk.likelabs.application.dto.statistics.Statistics;
import com.redwerk.likelabs.application.dto.statistics.StatisticsType;

public interface StatisticsService {
    
    Statistics getStatistics(StatisticsType type);

}
