package spring.test.jms.service.Impl;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.test.jms.dao.EventDao;
import spring.test.jms.dao.TicketDao;
import spring.test.jms.dao.UserDao;
import spring.test.jms.model.Ticket;
import spring.test.jms.model.impl.TicketImpl;
import spring.test.jms.service.TicketService;
import spring.test.jms.util.IdGenerator;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Setter
@Service
public class TicketServiceImpl implements TicketService {
    private final TicketDao ticketDao;
    private final EventDao eventDao;
    private final UserDao userDao;
    private IdGenerator idGenerator;

    @Autowired
    public TicketServiceImpl(TicketDao ticketDao, EventDao eventDao, UserDao userDao, IdGenerator idGenerator) {
        this.ticketDao = ticketDao;
        this.eventDao = eventDao;
        this.userDao = userDao;
        this.idGenerator = idGenerator;
    }

    @Override
    public Ticket bookTicket(Ticket ticket) {
        boolean isPlaceBooked = ticketDao.isPlaceBooked(ticket.getPlace());
        if (isPlaceBooked) {
            throw new IllegalStateException("This place has already been booked");
        }

        long generatedId = idGenerator.generateId(TicketImpl.class);
        ticket.setId(generatedId);
        log.info("Booking ticket with id " + generatedId);
        ticketDao.add(ticket);
        log.info("Ticket was booked successfully");
        return ticket;
    }

    @Override
    public List<Ticket> getBookedTicketsByUser(long userId, int pageSize, int pageNum) {
        log.info("Getting booked tickets by user");
        List<Ticket> bookedTickets = ticketDao
                .getBookedTicketsByUserId(userId, pageSize, pageNum)
                .stream()
                .map(ticket -> Pair.of(eventDao.getById(ticket.getEventId()).get(), ticket))
                .sorted(Comparator.comparing(pair -> pair.getLeft().getDate(), Comparator.reverseOrder()))
                .map(Pair::getRight)
                .collect(Collectors.toList());
        log.info("Tickets were got successfully");

        return bookedTickets;
    }

    @Override
    public List<Ticket> getBookedTicketsByEvent(long eventId, int pageSize, int pageNum) {
        log.info("Getting booked tickets by event");
        List<Ticket> bookedTickets = ticketDao
                .getBookedTicketsByEventId(eventId, pageSize, pageNum)
                .stream()
                .map(ticket -> ImmutablePair.of(userDao.getUserById(ticket.getUserId()).get(), ticket))
                .sorted(Comparator.comparing(pair -> pair.getLeft().getEmail()))
                .map(Pair::getRight)
                .collect(Collectors.toList());
        log.info("Tickets were got successfully");
        return bookedTickets;
    }

    @Override
    public boolean cancelTicket(long ticketId) {
        return ticketDao.deleteTicket(ticketId);
    }
}
