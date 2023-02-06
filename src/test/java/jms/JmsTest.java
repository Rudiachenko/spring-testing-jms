package jms;

import org.apache.activemq.junit.EmbeddedActiveMQBroker;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import spring.test.jms.config.AppConfig;
import spring.test.jms.controller.BookingController;
import spring.test.jms.jms.MessageListener;
import spring.test.jms.model.Ticket;
import spring.test.jms.model.impl.TicketImpl;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = AppConfig.class, loader = AnnotationConfigContextLoader.class)
public class JmsTest {
    private static final Long USER_ID = 1L;
    private static final Long EVENT_PD = 1L;
    private static final Integer PLACE = 1;
    private static final String CATEGORY = "Bar";
    private static final String PREFIX = "queue://";
    private static final String TICKET_IN_STRING_REPRESENTATION = "{\"id\":0,\"eventId\":1,\"userId\":1,\"place\":1,\"category\":\"BAR\"}";
    @ClassRule
    public static EmbeddedActiveMQBroker embeddedBroker = new EmbeddedActiveMQBroker();

    @SpyBean
    private MessageListener messageListener;

    @SpyBean
    private JmsTemplate jmsTemplate;

    @Autowired
    private BookingController bookingController;

    @Test
    public void shouldSendMessageWithTicket() throws JMSException {
        bookingController.bookTicketAsync(USER_ID, EVENT_PD, PLACE, CATEGORY);
        String queueName = "TicketQueue";
        Message message = embeddedBroker.peekMessage(queueName);

        assertEquals(1, embeddedBroker.getMessageCount(queueName));
        assertEquals(PREFIX + queueName, message.getJMSDestination().toString());
        assertTrue(message.toString().contains(TICKET_IN_STRING_REPRESENTATION));
    }

    @Test
    public void whenListeningThenReceivingTicket() {
        String queueName = "TicketQueue";
        Ticket ticket = createTicket();

        jmsTemplate.convertAndSend(queueName, ticket);

        ArgumentCaptor<TicketImpl> messageCaptor = ArgumentCaptor.forClass(TicketImpl.class);

        Mockito.verify(messageListener, Mockito.timeout(100))
                .jmsListenerBookingTicket(messageCaptor.capture());

        Ticket ticketFromCaptor = messageCaptor.getValue();
        assertEquals(ticketFromCaptor, ticket);
    }

    @Test
    public void whenListeningThenReceivingCorrectMessage() throws JMSException {
        String queueName = "TestQueue";
        String messageText = "Test message";

        embeddedBroker.pushMessage(queueName, messageText);
        assertEquals(1, embeddedBroker.getMessageCount(queueName));

        ArgumentCaptor<TextMessage> messageCaptor = ArgumentCaptor.forClass(TextMessage.class);

        Mockito.verify(messageListener, Mockito.timeout(100))
                .jmsListenerMethod(messageCaptor.capture());

        TextMessage receivedMessage = messageCaptor.getValue();
        assertEquals(messageText, receivedMessage.getText());
    }

    private Ticket createTicket() {
        return TicketImpl.builder()
                .userId(USER_ID)
                .eventId(EVENT_PD)
                .place(PLACE)
                .category(Ticket.Category.BAR).build();
    }
}
