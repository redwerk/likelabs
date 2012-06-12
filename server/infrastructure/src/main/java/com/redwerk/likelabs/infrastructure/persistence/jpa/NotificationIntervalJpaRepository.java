package com.redwerk.likelabs.infrastructure.persistence.jpa;

import com.redwerk.likelabs.domain.model.event.EventType;
import com.redwerk.likelabs.domain.model.notification.NotificationInterval;
import com.redwerk.likelabs.domain.model.notification.NotificationIntervalRepository;
import com.redwerk.likelabs.domain.model.notification.WarningType;
import com.redwerk.likelabs.infrastructure.persistence.jpa.util.EntityJpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.Map;

@Repository
public class NotificationIntervalJpaRepository extends NotificationIntervalRepository {

    private static final String GET_PARAMETER_BY_EVENT_TYPE =
            "select n from NotificationInterval n where n.eventType = :eventType";

    private static final String GET_PARAMETER_BY_WARNING_TYPE =
            "select n from NotificationInterval n where n.eventType = :eventType";

    @PersistenceContext
    private EntityManager em;

    private EntityJpaRepository<NotificationInterval, Long> entityRepository;


    @Override
    public NotificationInterval find(EventType eventType) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("eventType", eventType);
        return getEntityRepository().findSingleEntity(GET_PARAMETER_BY_EVENT_TYPE, parameters);
    }

    @Override
    public NotificationInterval find(WarningType warningType) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("warningType", warningType);
        return getEntityRepository().findSingleEntity(GET_PARAMETER_BY_WARNING_TYPE, parameters);
    }

    @Override
    public void add(NotificationInterval interval) {
        getEntityRepository().add(interval);
    }

    private EntityJpaRepository<NotificationInterval, Long> getEntityRepository() {
        if (entityRepository == null) {
            entityRepository = new EntityJpaRepository<NotificationInterval, Long>(em, NotificationInterval.class);
        }
        return entityRepository;
    }

}
