package service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import spring.test.jms.dao.EventDao;
import spring.test.jms.dao.TicketDao;
import spring.test.jms.dao.UserDao;
import spring.test.jms.model.Event;
import spring.test.jms.model.Ticket;
import spring.test.jms.model.User;
import spring.test.jms.model.impl.EventImpl;
import spring.test.jms.model.impl.TicketImpl;
import spring.test.jms.model.impl.UserImpl;
import spring.test.jms.service.Impl.TicketServiceImpl;
import spring.test.jms.util.IdGenerator;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {
    @Mock
    private IdGenerator idGenerator;
    @Mock
    private EventDao eventDao;
    @Mock
    private UserDao userDao;
    @Mock
    private TicketDao ticketDao;
    @InjectMocks
    private TicketServiceImpl sut;

    @Test
    void shouldBookWhenExistIdsPassedAndBookingNotAlreadyDone() {
        //given
        Ticket ticket = createTicket(1, 1, 1, Ticket.Category.PREMIUM, 2);

        when(idGenerator.generateId(TicketImpl.class)).thenReturn(1L);
        when(ticketDao.isPlaceBooked(anyLong())).thenReturn(false);
        when(ticketDao.add(any(TicketImpl.class))).thenReturn(ticket);

        //when
        Ticket actual = sut.bookTicket(ticket);

        //then
        assertThat(actual, is(ticket));
    }

    @Test
    void shouldThrowIllegalStateExceptionWhenBookingAlreadyDone() {
        Ticket ticket = createTicket(1, 1, 1, Ticket.Category.PREMIUM, 2);

        when(ticketDao.isPlaceBooked(2)).thenReturn(true);

        //then
        assertThrows(IllegalStateException.class, () -> sut.bookTicket(ticket));
    }

    @Test
    void shouldGetBookingsByUser() {
        //given
        int pageSize = 5;
        int pageNumber = 2;

        User user = createUser(1, "Bob", "Bob@gmail.com");

        List<Ticket> tickets = Arrays.asList(createTicket(1, 1, 2, Ticket.Category.PREMIUM, 2),
                createTicket(3, 1, 1, Ticket.Category.BAR, 5),
                createTicket(10, 1, 3, Ticket.Category.STANDARD, 10));

        List<Event> events = Arrays.asList(createEvent(1, "New Year", new Date(123)),
                createEvent(2, "New Year After-party", new Date(233)),
                createEvent(3, "New Year Pre_party", new Date(344)));

        List<Ticket> expected = Arrays.asList(createTicket(10, 1, 3, Ticket.Category.STANDARD, 10),
                createTicket(1, 1, 2, Ticket.Category.PREMIUM, 2),
                createTicket(3, 1, 1, Ticket.Category.BAR, 5));

        when(ticketDao.getBookedTicketsByUserId(user.getId(), pageSize, pageNumber)).thenReturn(tickets);
        when(eventDao.getById(1)).thenReturn(Optional.of(events.get(0)));
        when(eventDao.getById(2)).thenReturn(Optional.of(events.get(1)));
        when(eventDao.getById(3)).thenReturn(Optional.of(events.get(2)));

        //when
        List<Ticket> actual = sut.getBookedTicketsByUser(user.getId(), pageSize, pageNumber);

        //then
        assertThat(actual, is(expected));
    }

    @Test
    void shouldGetBookingsByEvent() {
        //given
        int pageSize = 5;
        int pageNumber = 2;

        Event event = createEvent(1, "New Year", new Date(123));

        List<Ticket> tickets = Arrays.asList(createTicket(1, 1, 1, Ticket.Category.PREMIUM, 2),
                createTicket(3, 2, 1, Ticket.Category.BAR, 5),
                createTicket(10, 3, 1, Ticket.Category.STANDARD, 10));

        List<User> users = Arrays.asList(createUser(1, "Bob1", "Bob1@gmail.eu"),
                createUser(2, "Bob2", "Bob2@gmail.eu"),
                createUser(3, "Bob3", "Bob3@gmail.eu"));

        List<Ticket> expected = Arrays.asList(createTicket(1, 1, 1, Ticket.Category.PREMIUM, 2),
                createTicket(3, 2, 1, Ticket.Category.BAR, 5),
                createTicket(10, 3, 1, Ticket.Category.STANDARD, 10));

        when(ticketDao.getBookedTicketsByEventId(event.getId(), pageSize, pageNumber)).thenReturn(tickets);
        when(userDao.getUserById(1)).thenReturn(Optional.of(users.get(0)));
        when(userDao.getUserById(2)).thenReturn(Optional.of(users.get(1)));
        when(userDao.getUserById(3)).thenReturn(Optional.of(users.get(2)));

        //when
        List<Ticket> actual = sut.getBookedTicketsByEvent(event.getId(), pageSize, pageNumber);

        //then
        assertThat(actual, is(expected));
    }

    @Test
    void shouldDeleteEventWhenExistIdPassed() {
        //given
        when(ticketDao.deleteTicket(1)).thenReturn(true);

        //when
        boolean actual = sut.cancelTicket(1);

        //then
        assertTrue(actual);
    }

    @Test
    void shouldNotDeleteEventWhenNotExistIdPassed() {
        //given
        when(ticketDao.deleteTicket(anyLong())).thenReturn(false);

        //when
        boolean actual = sut.cancelTicket(10);

        //then
        assertFalse(actual);
    }

    private Event createEvent(long id, String title, Date date) {
        return EventImpl.builder()
                .id(id)
                .title(title)
                .date(date)
                .build();
    }

    private UserImpl createUser(long id, String name, String email) {
        return UserImpl.builder()
                .id(id)
                .name(name)
                .email(email)
                .build();
    }

    private Ticket createTicket(long id, long userId, long eventId, Ticket.Category category, int place) {
        return TicketImpl.builder()
                .id(id)
                .userId(userId)
                .eventId(eventId)
                .category(category)
                .place(place)
                .build();
    }
}