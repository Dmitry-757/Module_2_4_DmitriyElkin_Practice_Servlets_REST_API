package com.DmitryElkin_Servlets_REST_API.repository;

import com.DmitryElkin_Servlets_REST_API.model.Event;

public class EventRepository extends HibernateRepository<Event> {
    public EventRepository() {
        super(Event.class);
    }
}
