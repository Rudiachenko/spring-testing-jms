package spring.test.jms.service.Impl;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.test.jms.dao.EventDao;
import spring.test.jms.model.Event;
import spring.test.jms.model.impl.EventImpl;
import spring.test.jms.service.EventService;
import spring.test.jms.util.IdGenerator;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Setter
@Service
public class EventServiceImpl implements EventService {
    private final EventDao eventDao;
    private IdGenerator idGenerator;

    @Autowired
    public EventServiceImpl(EventDao eventDao, IdGenerator idGenerator) {
        this.eventDao = eventDao;
        this.idGenerator = idGenerator;
    }

    @Override
    public Event createEvent(Event event) {
        long generatedId = idGenerator.generateId(EventImpl.class);
        log.info("Creating event with id " + generatedId);
        event.setId(generatedId);
        eventDao.add(event);
        log.info("Event was created successfully");
        return event;
    }

    @Override
    public Event getById(long id) {
        log.info("Getting event with id: " + id);
        Optional<Event> eventById = eventDao.getById(id);
        if (eventById.isPresent()) {
            log.info("Event was got successfully: " + eventById.get());
            return eventById.get();
        } else {
            throw new NoSuchElementException("Can't get event with id: " + id);
        }
    }

    @Override
    public List<Event> getAll() {
        log.info("Getting all events");
        List<Event> events = eventDao.getAll();
        if (events.isEmpty()) {
            log.info("No events are presented");
        } else {
            log.info("Events were got successfully: " + events);
        }
        return events;
    }

    @Override
    public List<Event> getEventsByTitle(String title, int pageSize, int pageNum) {
        log.info("Getting events by title " + title);
        List<Event> eventsByTitle = eventDao.getEventsByTitle(title, pageSize, pageNum);
        log.info("Getting with defined title " + eventsByTitle.toString());
        return eventsByTitle;
    }

    @Override
    public List<Event> getEventsForDay(Date day, int pageSize, int pageNum) {
        log.info("Getting events by day " + day.toString());

        List<Event> eventsForDay = eventDao.getEventsForDay(day, pageSize, pageNum);

        log.info("Events in this day " + eventsForDay.toString());
        return eventsForDay;
    }

    @Override
    public Event updateEvent(Event event, long id) {
        log.info("Updating event with id " + id);
        Optional<Event> eventFromDb = eventDao.updateEvent(event, id);
        if (eventFromDb.isPresent()) {
            log.info("Event was updated successfully");
            return eventFromDb.get();
        } else {
            throw new NoSuchElementException("Can't update event with id " + event.getId());
        }
    }

    @Override
    public boolean deleteEvent(long eventId) {
        log.info("Deleting event with id " + eventId);
        boolean isDeleted = eventDao.deleteEvent(eventId);
        if (isDeleted) {
            log.info("Event was deleted successfully");
            return true;
        }
        log.info("No event was found with such id  " + eventId);
        return false;
    }
}
