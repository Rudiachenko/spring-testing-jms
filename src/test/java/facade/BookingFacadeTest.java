package facade;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import spring.test.jms.config.AppConfig;
import spring.test.jms.facade.Impl.BookingFacadeImpl;
import spring.test.jms.model.Event;
import spring.test.jms.model.Ticket;
import spring.test.jms.model.User;
import spring.test.jms.model.impl.EventImpl;
import spring.test.jms.model.impl.TicketImpl;
import spring.test.jms.model.impl.UserImpl;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppConfig.class, loader = AnnotationConfigContextLoader.class)
public class BookingFacadeTest {
    @Autowired
    private BookingFacadeImpl bookingFacadeImpl;

    @Test
    void shouldCreateAndAddObjectsToStorage() {
        User user = createUser("Bob", "bob@gmail.com");
        User createdUser = bookingFacadeImpl.createUser(user);
        assertThat(createdUser.getId(), is(5L));

        Event event = createEvent("NewEvent", new Date(333));
        Event createdEvent = bookingFacadeImpl.createEvent(event);
        assertThat(createdEvent.getId(), is(1L));

        Ticket createdTicket = createTicket(createdUser, createdEvent, 5, Ticket.Category.STANDARD);
        Ticket ticket = bookingFacadeImpl.bookTicket(createdTicket);
        assertThat(ticket.getId(), is(1L));
        assertThat(ticket.getUserId(), is(5L));
        assertThat(ticket.getEventId(), is(1L));
        assertThat(ticket.getCategory(), is(Ticket.Category.STANDARD));
        assertThat(ticket.getPlace(), is(5));

        List<Ticket> bookedTickets = bookingFacadeImpl.getBookedTicketsByUserId(createdUser.getId(), 10, 1);
        assertThat(bookedTickets, is(Collections.singletonList(ticket)));

        boolean isCanceled = bookingFacadeImpl.cancelTicket(ticket.getId());
        List<Ticket> bookedTicketsAfterCancellation = bookingFacadeImpl.getBookedTicketsByUserId(createdUser.getId(), 10, 1);
        assertTrue(isCanceled);
        assertThat(bookedTicketsAfterCancellation, is(Collections.emptyList()));
    }

    private Ticket createTicket(User user, Event event, int place, Ticket.Category category) {
        return TicketImpl.builder()
                .userId(user.getId())
                .eventId(event.getId())
                .place(place)
                .category(category)
                .build();
    }

    private User createUser(String name, String email) {
        return UserImpl.builder()
                .name(name)
                .email(email)
                .build();
    }

    private Event createEvent(String title, Date date) {
        return EventImpl.builder()
                .title(title)
                .date(date)
                .build();
    }
}
