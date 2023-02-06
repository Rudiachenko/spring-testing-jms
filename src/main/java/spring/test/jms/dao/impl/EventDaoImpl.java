package spring.test.jms.dao.impl;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import spring.test.jms.dao.EventDao;
import spring.test.jms.model.Event;
import spring.test.jms.storage.impl.EventStorageImpl;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@Setter
@Repository
public class EventDaoImpl implements EventDao {
    private final EventStorageImpl eventStorage;

    @Autowired
    public EventDaoImpl(EventStorageImpl eventStorage) {
        this.eventStorage = eventStorage;
    }

    @Override
    public Event add(Event event) {
        return eventStorage.add(event);
    }

    @Override
    public Optional<Event> getById(long id) {
        return eventStorage.getById(id);
    }

    @Override
    public List<Event> getAll() {
        return eventStorage.getAll();
    }

    @Override
    public List<Event> getEventsByTitle(String title, int pageSize, int pageNum) {
        return eventStorage.getEventsByTitle(title, pageSize, pageNum);
    }

    @Override
    public List<Event> getEventsForDay(Date day, int pageSize, int pageNum) {
        return eventStorage.getEventsForDay(day, pageSize, pageNum);
    }

    @Override
    public Optional<Event> updateEvent(Event event, long id) {
        return eventStorage.updateEvent(event, id);
    }

    @Override
    public boolean deleteEvent(long eventId) {
        return eventStorage.deleteEvent(eventId);
    }

}
