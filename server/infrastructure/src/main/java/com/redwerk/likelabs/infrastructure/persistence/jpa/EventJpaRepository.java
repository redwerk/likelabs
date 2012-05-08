package com.redwerk.likelabs.infrastructure.persistence.jpa;

import com.redwerk.likelabs.domain.model.event.Event;
import com.redwerk.likelabs.domain.model.event.EventRepository;
import com.redwerk.likelabs.domain.model.event.EventStatus;
import com.redwerk.likelabs.domain.model.user.User;
import com.redwerk.likelabs.infrastructure.persistence.jpa.util.EntityJpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.redwerk.likelabs.domain.model.event.EventStatus.*;

@Repository
public class EventJpaRepository implements EventRepository {

    private static final String GET_ALL_EVENTS = "select e from Event e";

    private static final String GET_CREATED_EVENTS =
            GET_ALL_EVENTS + " where e.notifiedDT is null order by createdDT desc";

    private static final String GET_NOTIFIED_EVENTS =
            GET_ALL_EVENTS + " where e.notifiedDT is not null order by notifiedDT desc, createdDT desc";
    
    private static Map<EventStatus, String> queries = new HashMap<EventStatus, String>() {{
        put(CREATED, GET_CREATED_EVENTS);
        put(NOTIFIED, GET_NOTIFIED_EVENTS);
        put(ALL, GET_ALL_EVENTS);
    }};

    @PersistenceContext
    private EntityManager em;

    private EntityJpaRepository<Event, Long> entityRepository;


    @Override
    public Event find(Long id) {
        return getEntityRepository().findById(id);
    }

    @Override
    public List<Event> findAll(User user, EventStatus status) {
        return getEntityRepository().findEntityList(queries.get(status));
    }

    @Override
    public void add(Event event) {
        getEntityRepository().add(event);
    }

    @Override
    public void remove(Event event) {
        getEntityRepository().remove(event);
    }

    private EntityJpaRepository<Event, Long> getEntityRepository() {
        if (entityRepository == null) {
            entityRepository = new EntityJpaRepository<Event, Long>(em, Event.class);
        }
        return entityRepository;
    }

}
