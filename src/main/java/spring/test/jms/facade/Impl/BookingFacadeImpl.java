package spring.test.jms.facade.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.test.jms.facade.BookingFacade;
import spring.test.jms.model.Event;
import spring.test.jms.model.Ticket;
import spring.test.jms.model.User;
import spring.test.jms.service.EventService;
import spring.test.jms.service.TicketService;
import spring.test.jms.service.UserService;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BookingFacadeImpl implements BookingFacade {
    private final EventService eventService;
    private final TicketService ticketService;
    private final UserService userService;

    @Override
    public Event createEvent(Event event) {
        return eventService.createEvent(event);
    }

    @Override
    public Event getEventById(long eventId) {
        return eventService.getById(eventId);
    }

    @Override
    public List<Event> getEventsByTitle(String title, int pageSize, int pageNum) {
        return eventService.getEventsByTitle(title, pageSize, pageNum);
    }

    @Override
    public List<Event> getEventsForDay(Date day, int pageSize, int pageNum) {
        return eventService.getEventsForDay(day, pageSize, pageNum);
    }

    @Override
    public Event updateEvent(Event event, long id) {
        return eventService.updateEvent(event, id);
    }

    @Override
    public boolean deleteEvent(long eventId) {
        return eventService.deleteEvent(eventId);
    }

    @Override
    public User getUserById(long userId) {
        return userService.getUserById(userId);
    }

    @Override
    public User getUserByEmail(String email) {
        return userService.getUserByEmail(email);
    }

    @Override
    public List<User> getUsersByName(String name, int pageSize, int pageNum) {
        return userService.getUsersByName(name, pageSize, pageNum);
    }

    @Override
    public User createUser(User user) {
        return userService.create(user);
    }

    @Override
    public User updateUser(User user, long id) {
        return userService.updateUser(user, id);
    }

    @Override
    public boolean deleteUser(long userId) {
        return userService.deleteUser(userId);
    }

    @Override
    public Ticket bookTicket(Ticket ticket) {
        return ticketService.bookTicket(ticket);
    }

    @Override
    public List<Ticket> getBookedTicketsByUserId(long userId, int pageSize, int pageNum) {
        return ticketService.getBookedTicketsByUser(userId, pageSize, pageNum);
    }

    @Override
    public List<Ticket> getBookedTicketsByEventId(long eventId, int pageSize, int pageNum) {
        return ticketService.getBookedTicketsByEvent(eventId, pageSize, pageNum);
    }

    @Override
    public boolean cancelTicket(long ticketId) {
        return ticketService.cancelTicket(ticketId);
    }
}
