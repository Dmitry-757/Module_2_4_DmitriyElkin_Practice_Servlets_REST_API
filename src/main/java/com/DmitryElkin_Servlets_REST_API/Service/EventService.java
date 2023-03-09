package com.DmitryElkin_Servlets_REST_API.Service;

import com.DmitryElkin_Servlets_REST_API.model.Event;
import com.DmitryElkin_Servlets_REST_API.model.File;
import com.DmitryElkin_Servlets_REST_API.model.User;

import java.util.ArrayList;
import java.util.List;

public class EventService {
    public void newEvent(User user, File file){
        Event newEvent = new Event(user, file);


        List<Event> events = new ArrayList<>();
        events = user.getEvents();
        events.add(newEvent);
        user.setEvents(events);

        // TO DO
        // save new event to BD
        // save changed user to BD


    }
}
