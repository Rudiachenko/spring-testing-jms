package spring.test.jms.storage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spring.test.jms.model.Event;
import spring.test.jms.storage.EventStorage;
import spring.test.jms.util.Paginator;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
public class EventStorageImpl implements EventStorage {
    private final Map<Long, Event> eventStorageMap = new HashMap<>();
    private final Paginator<Event> paginator;

    @Autowired
    public EventStorageImpl(Paginator<Event> paginator) {
        this.paginator = paginator;
    }

    @Override
    public Event add(Event event) {
        return eventStorageMap.put(event.getId(), event);
    }

    @Override
    public Optional<Event> getById(long id) {
        return Optional.ofNullable(eventStorageMap.get(id));
    }

    @Override
    public List<Event> getAll() {
        return new ArrayList<>(eventStorageMap.values());
    }

    @Override
    public List<Event> getEventsByTitle(String title, int pageSize, int pageNum) {
        Predicate<Event> predicate = event -> event.getTitle().contains(title);
        return paginator.paginate(getMatchingEvents(predicate), pageSize, pageNum);
    }

    @Override
    public List<Event> getEventsForDay(Date day, int pageSize, int pageNum) {
        Predicate<Event> predicate = event -> event.getDate().equals(day);
        return paginator.paginate(getMatchingEvents(predicate), pageSize, pageNum);
    }

    @Override
    public Optional<Event> updateEvent(Event event, long id) {
        return Optional.ofNullable(eventStorageMap.replace(id, event));
    }

    @Override
    public boolean deleteEvent(long eventId) {
        if (eventStorageMap.containsKey(eventId)) {
            eventStorageMap.remove(eventId);
            return true;
        }
        return false;
    }

    private List<Event> getMatchingEvents(Predicate<Event> predicate) {
        return eventStorageMap.values().stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }
}
