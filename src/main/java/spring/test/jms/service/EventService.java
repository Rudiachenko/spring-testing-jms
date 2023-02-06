package spring.test.jms.service;


import spring.test.jms.model.Event;

import java.util.Date;
import java.util.List;

public interface EventService {
    Event createEvent(Event event);

    Event getById(long id);

    List<Event> getAll();

    List<Event> getEventsByTitle(String title, int pageSize, int pageNum);

    List<Event> getEventsForDay(Date day, int pageSize, int pageNum);

    Event updateEvent(Event event, long id);

    boolean deleteEvent(long eventId);
}
