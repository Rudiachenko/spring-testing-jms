package spring.test.jms.dao.impl;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import spring.test.jms.dao.TicketDao;
import spring.test.jms.model.Ticket;
import spring.test.jms.storage.TicketStorage;

import java.util.List;


@Setter
@Repository
public class TicketDaoImpl implements TicketDao {
    private final TicketStorage ticketStorage;

    @Autowired
    public TicketDaoImpl(TicketStorage ticketStorage) {
        this.ticketStorage = ticketStorage;
    }

    @Override
    public List<Ticket> getAllTickets() {
        return ticketStorage.getAllTickets();
    }

    @Override
    public Ticket add(Ticket ticket) {
        return ticketStorage.add(ticket);
    }

    @Override
    public List<Ticket> getBookedTicketsByUserId(long userId, int pageSize, int pageNum) {
        return ticketStorage.getBookedTicketsByUserId(userId, pageSize, pageNum);
    }

    @Override
    public List<Ticket> getBookedTicketsByEventId(long eventId, int pageSize, int pageNum) {
        return ticketStorage.getBookedTicketsByEventId(eventId, pageSize, pageNum);
    }

    @Override
    public boolean deleteTicket(long ticketId) {
        return ticketStorage.deleteTicket(ticketId);
    }

    @Override
    public boolean isPlaceBooked(long place) {
        return ticketStorage.getAllTickets().stream()
                .anyMatch(ticket -> ticket.getPlace() == place);
    }
}
