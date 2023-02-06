package spring.test.jms.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import spring.test.jms.facade.BookingFacade;
import spring.test.jms.model.Event;
import spring.test.jms.model.Ticket;
import spring.test.jms.model.User;
import spring.test.jms.model.dto.EventRequestDto;
import spring.test.jms.model.dto.UserRequestDto;
import spring.test.jms.service.mappers.EventMapper;
import spring.test.jms.service.mappers.TicketMapper;
import spring.test.jms.service.mappers.UserMapper;

import java.util.Date;
import java.util.List;

@Slf4j
@RestController
public class BookingController {
    private final BookingFacade bookingFacade;
    private final TicketMapper ticketMapper;
    private final UserMapper userMapper;
    private final EventMapper eventMapper;
    private final JmsTemplate jmsTemplate;

    @Autowired
    public BookingController(BookingFacade bookingFacade, UserMapper userMapper,
                             EventMapper eventMapper, JmsTemplate jmsTemplate, TicketMapper ticketMapper) {
        this.bookingFacade = bookingFacade;
        this.userMapper = userMapper;
        this.eventMapper = eventMapper;
        this.ticketMapper = ticketMapper;
        this.jmsTemplate = jmsTemplate;
    }

    @PostMapping("/event/add")
    public Event createEvent(@RequestBody EventRequestDto requestDto) {
        return bookingFacade.createEvent(eventMapper.mapEventRequestDtoToEvent(requestDto));
    }

    @GetMapping("/event/{eventId}")
    public Event getEventById(@PathVariable long eventId) {
        return bookingFacade.getEventById(eventId);
    }

    @GetMapping("/event/{title}")
    public List<Event> getEventsByTitle(@PathVariable String title,
                                        @RequestParam(value = "pageSize") int pageSize,
                                        @RequestParam(value = "pageNum") int pageNum) {
        return bookingFacade.getEventsByTitle(title, pageSize, pageNum);
    }

    @GetMapping("/event")
    public List<Event> getEventsForDay(@RequestParam(value = "day") Date day,
                                       @RequestParam(value = "pageSize") int pageSize,
                                       @RequestParam(value = "pageNum") int pageNum) {
        return bookingFacade.getEventsForDay(day, pageSize, pageNum);
    }

    @PostMapping("/event/update/{id}")
    public Event updateEvent(@RequestBody EventRequestDto requestDto, @PathVariable long id) {
        return bookingFacade.updateEvent(eventMapper.mapEventRequestDtoToEvent(requestDto, id), id);
    }

    @GetMapping("/user/{userId}")
    public User getUserById(@PathVariable long userId) {
        return bookingFacade.getUserById(userId);
    }

    @GetMapping("/user/{email}")
    public User getUserByEmail(@PathVariable String email) {
        return bookingFacade.getUserByEmail(email);
    }

    @GetMapping("/user/{name}")
    public List<User> getUsersByName(@PathVariable String name,
                                     @RequestParam(value = "pageSize") int pageSize,
                                     @RequestParam(value = "pageNum") int pageNum) {
        return bookingFacade.getUsersByName(name, pageSize, pageNum);
    }

    @PostMapping("/user/add")
    public User createUser(@RequestBody UserRequestDto userRequestDto) {
        return bookingFacade.createUser(userMapper.mapUserRequestDtoToUser(userRequestDto));
    }

    @PostMapping("/user/update/{id}")
    public User updateUser(@RequestBody UserRequestDto userRequestDto, @PathVariable long id) {
        return bookingFacade.updateUser(userMapper.mapUserRequestDtoToUser(userRequestDto, id), id);
    }

    @PostMapping("/ticket/book/")
    public Ticket bookTicket(@RequestParam(value = "userId") long userId,
                             @RequestParam(value = "eventId") long eventId,
                             @RequestParam(value = "place") int place,
                             @RequestParam(value = "category") String category) {
        Ticket ticket = ticketMapper.mapFieldsToTicket(userId, eventId, place, category);

        return bookingFacade.bookTicket(ticket);
    }

    @PostMapping("/ticket/async/book/")
    public Ticket bookTicketAsync(@RequestParam(value = "userId") long userId,
                                  @RequestParam(value = "eventId") long eventId,
                                  @RequestParam(value = "place") int place,
                                  @RequestParam(value = "category") String category) {
        Ticket ticket = ticketMapper.mapFieldsToTicket(userId, eventId, place, category);
        jmsTemplate.convertAndSend("TicketQueue", ticket);
        return ticket;
    }

    @GetMapping("/ticket/get/user/{userId}")
    public List<Ticket> getBookedTicketsByUserId(@PathVariable long userId,
                                                 @RequestParam(value = "pageSize") int pageSize,
                                                 @RequestParam(value = "pageNum") int pageNum) {
        return bookingFacade.getBookedTicketsByUserId(userId, pageSize, pageNum);
    }

    @GetMapping("/ticket/get/event/{eventId}")
    public List<Ticket> getBookedTicketsByEventId(@PathVariable long eventId,
                                                  @RequestParam(value = "pageSize") int pageSize,
                                                  @RequestParam(value = "pageNum") int pageNum) {
        return bookingFacade.getBookedTicketsByEventId(eventId, pageSize, pageNum);
    }
}
