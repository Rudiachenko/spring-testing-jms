package spring.test.jms.dao;

import spring.test.jms.model.Ticket;

import java.util.List;

public interface TicketDao {
    List<Ticket> getAllTickets();

    Ticket add(Ticket ticket);

    List<Ticket> getBookedTicketsByUserId(long userId, int pageSize, int pageNum);

    List<Ticket> getBookedTicketsByEventId(long eventId, int pageSize, int pageNum);

    boolean deleteTicket(long ticketId);

    boolean isPlaceBooked(long place);
}
