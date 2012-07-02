package com.redwerk.likelabs.application.impl.statistics;

import com.redwerk.likelabs.application.StatisticsService;
import com.redwerk.likelabs.application.dto.statistics.*;
import com.redwerk.likelabs.application.dto.statistics.ParameterType;
import com.redwerk.likelabs.domain.model.notification.Period;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumMap;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticsServiceMockImpl implements StatisticsService {

    private final Map<StatisticsType, TotalsStatistics> statistics = new HashMap<StatisticsType, TotalsStatistics>() {{
        put(StatisticsType.GENERAL, new TotalsStatistics(new ArrayList<Parameter>() {{
            add(new Parameter(ParameterType.PHOTOS_TAKEN, new Totals(3423, 132, 642, 1642)));
            add(new Parameter(ParameterType.FACEBOOK_POSTS, new Totals(2321, 65, 324, 1222)));
            add(new Parameter(ParameterType.VKONTAKTE_POSTS, new Totals(987, 34, 232, 675)));
            add(new Parameter(ParameterType.EMAILS, new Totals(343, 23, 132, 203)));
        }}));
        put(StatisticsType.FACEBOOK, new TotalsStatistics(new ArrayList<Parameter>() {{
            add(new Parameter(ParameterType.POSTS, new Totals(2321, 65, 324, 1222)));
            add(new Parameter(ParameterType.LIKES, new Totals(542, 65, 342, 1543)));
            add(new Parameter(ParameterType.COMMENTS, new Totals(231, 34, 142, 675)));
            add(new Parameter(ParameterType.SHARES, new Totals(214, 41, 122, 203)));
        }}));
        put(StatisticsType.VKONTAKTE, new TotalsStatistics(new ArrayList<Parameter>() {{
            add(new Parameter(ParameterType.POSTS, new Totals(987, 34, 232, 675)));
            add(new Parameter(ParameterType.LIKES, new Totals(432, 37, 182, 967)));
            add(new Parameter(ParameterType.COMMENTS, new Totals(452, 23, 116, 310)));
            add(new Parameter(ParameterType.SHARES, new Totals(121, 19, 22, 58)));
        }}));
    }};

    @Override
    public TotalsStatistics getStatistics(long companyId, StatisticsType statisticsType) {
        return statistics.get(statisticsType);
    }


    @Override
    public List<ChartPoint> getChartPoints(long companyId, final Interval interval) {
        return new  ArrayList<ChartPoint>(){{
            int period;
            Calendar c = new GregorianCalendar();
            switch (interval) {
                case DAYS_30: period = 1; c.add(Calendar.DATE, -30);break;
                case MONTHS_12: period = 12; c.add(Calendar.YEAR, -1);break;
                case MONTHS_6: period = 6; c.add(Calendar.MONTH, -6);break;
                default: period = 1;
            }
            for (int i=0; i<30; i++) {
                c.add(Calendar.DATE, period);
                add(new ChartPoint(c.getTime(), (int)(Math.random()*1000), (int)(Math.random()*1000), (int)(Math.random()*1000), (int)(Math.random()*1000)));
            }
        }};
    }
}
