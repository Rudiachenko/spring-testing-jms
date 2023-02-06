package spring.test.jms.service;


import spring.test.jms.model.Ticket;

import java.util.List;

public interface TicketService {
    Ticket bookTicket(Ticket ticket);

    List<Ticket> getBookedTicketsByUser(long userId, int pageSize, int pageNum);

    List<Ticket> getBookedTicketsByEvent(long eventId, int pageSize, int pageNum);

    boolean cancelTicket(long ticketId);
}
