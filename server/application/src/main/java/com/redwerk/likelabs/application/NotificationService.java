package com.redwerk.likelabs.application;

import com.redwerk.likelabs.application.dto.NotificationIntervalData;
import com.redwerk.likelabs.domain.model.notification.NotificationInterval;

import java.util.List;

public interface NotificationService {

    List<NotificationInterval> getIntervals();

    void updateIntervals(List<NotificationIntervalData> intervals);

}
