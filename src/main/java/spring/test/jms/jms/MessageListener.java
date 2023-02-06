package spring.test.jms.jms;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import spring.test.jms.exception.TicketBookingException;
import spring.test.jms.model.Ticket;
import spring.test.jms.model.impl.MessageImpl;
import spring.test.jms.service.MessageService;
import spring.test.jms.service.TicketService;

import javax.jms.JMSException;
import javax.jms.TextMessage;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MessageListener {
    private static int COUNT = 1;
    private final MessageService messageService;
    private final TicketService ticketService;

    @JmsListener(destination = "TicketQueue", containerFactory = "myFactory")
    public void jmsListenerBookingTicket(Ticket ticket) {
        String incomingMessage = "<" + COUNT + "> Received <" + ticket + ">";
        log.info(incomingMessage);
        bookTicket(ticket);
        addMessageToDB(incomingMessage);
        COUNT++;
    }

    // method for testing purpose
    @JmsListener(destination = "TestQueue", containerFactory = "myFactory")
    public void jmsListenerMethod(TextMessage message) throws JMSException {
        log.info("JMS listener received text message: {}", message.getText());
    }

    private void addMessageToDB(String content) {
        messageService.create(MessageImpl.builder()
                .content(content)
                .build());
    }

    private void bookTicket(Ticket ticket) {
        try {
            ticketService.bookTicket(ticket);
        } catch (Exception e) {
            throw new TicketBookingException("Can't book ticket");
        }
    }
}
