package spring.test.jms.service.mappers;

import org.springframework.stereotype.Component;
import spring.test.jms.exception.WrongTicketCategoryException;
import spring.test.jms.model.Ticket;
import spring.test.jms.model.impl.TicketImpl;

@Component
public class TicketMapper {
    private static final String BAR = "bar";
    private static final String PREMIUM = "premium";
    private static final String STANDARD = "standard";

    public Ticket mapFieldsToTicket(long userId, long eventId, int place, String category) {
        return TicketImpl.builder()
                .userId(userId)
                .eventId(eventId)
                .place(place)
                .category(getTicketCategory(category))
                .build();
    }

    private Ticket.Category getTicketCategory(String category) {
        if (category.equalsIgnoreCase(BAR)) {
            return Ticket.Category.BAR;
        } else if (category.equalsIgnoreCase(STANDARD)) {
            return Ticket.Category.STANDARD;
        } else if (category.equalsIgnoreCase(PREMIUM)) {
            return Ticket.Category.STANDARD;
        }
        throw new WrongTicketCategoryException("Wrong type of ticket category");
    }
}
