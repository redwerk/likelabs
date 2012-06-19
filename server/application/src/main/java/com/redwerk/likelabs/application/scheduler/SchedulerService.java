package com.redwerk.likelabs.application.scheduler;

import com.redwerk.likelabs.domain.model.event.EventRepository;
import com.redwerk.likelabs.domain.model.notification.NotificationIntervalRepository;
import com.redwerk.likelabs.domain.model.user.UserRepository;
import com.redwerk.likelabs.domain.service.UserNotifier;
import com.redwerk.likelabs.domain.service.notification.NotificationProcessor;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SchedulerService {
    
    private static final Logger LOGGER = Logger.getLogger(SchedulerService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private NotificationIntervalRepository intervalRepository;

    @Autowired
    private UserNotifier userNotifier;

    @Scheduled(cron="0 0 11 * * ?")
    @Transactional
    public void sendUsersNotifications() {
        LOGGER.info("processing of notifications was started");
        NotificationProcessor processor =
                new NotificationProcessor(userRepository, eventRepository, intervalRepository, userNotifier);
        processor.processPendingEvents();
        LOGGER.info("processing of notifications was finished");
    }
    
}
