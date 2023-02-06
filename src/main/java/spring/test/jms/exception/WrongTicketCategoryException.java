package spring.test.jms.exception;

public class WrongTicketCategoryException extends RuntimeException {
    public WrongTicketCategoryException(String message) {
        super(message);
    }
}
