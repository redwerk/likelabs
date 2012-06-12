package com.redwerk.likelabs.application.impl;

import com.redwerk.likelabs.application.NotificationService;
import com.redwerk.likelabs.application.dto.NotificationIntervalData;
import com.redwerk.likelabs.domain.model.notification.NotificationInterval;
import com.redwerk.likelabs.domain.model.notification.NotificationIntervalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationIntervalRepository repository;
    
    @Override
    @Transactional
    public List<NotificationInterval> getIntervals() {
        return repository.findAll();
    }

    @Override
    @Transactional
    public void updateIntervals(List<NotificationIntervalData> intervals) {
        for (NotificationIntervalData idata: intervals) {
            NotificationInterval i = (idata.getEventType() != null) ? repository.get(idata.getEventType()) :
                    repository.get(idata.getWarningType());
            i.setEmailInterval(idata.getEmailInterval());
            i.setSmsInterval(idata.getSmsInterval());
        }
    }

}
