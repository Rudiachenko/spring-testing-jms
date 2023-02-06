package spring.test.jms.exception;

public class TicketBookingException extends RuntimeException {
    public TicketBookingException(String message) {
        super(message);
    }
}
