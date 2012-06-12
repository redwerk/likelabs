package com.redwerk.likelabs.domain.model.notification;

import com.redwerk.likelabs.domain.model.event.EventType;

import java.util.ArrayList;
import java.util.List;

public abstract class NotificationIntervalRepository {

    public NotificationInterval get(EventType eventType) {
        NotificationInterval interval = find(eventType);
        if (interval == null) {
            interval = new NotificationInterval(eventType);
            add(interval);
        }
        return interval;
    }

    public NotificationInterval get(WarningType warningType) {
        NotificationInterval interval = find(warningType);
        if (interval == null) {
            interval = new NotificationInterval(warningType);
            if (warningType == WarningType.EMAIL_IS_ABSENT) {
                interval.setEmailInterval(Period.UNSUPPORTED);
            }
            add(interval);
        }
        return interval;
    }

    public List<NotificationInterval> findAll() {
        List<NotificationInterval> intervals = new ArrayList<NotificationInterval>();
        for (WarningType wt: WarningType.values()) {
            intervals.add(get(wt));
        }
        for (EventType et: EventType.values()) {
            intervals.add(get(et));
        }
        return intervals;
    }

    protected abstract NotificationInterval find(EventType eventType);

    protected abstract NotificationInterval find(WarningType warningType);

    protected abstract void add(NotificationInterval interval);


}
