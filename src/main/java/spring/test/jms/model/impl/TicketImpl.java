package spring.test.jms.model.impl;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import spring.test.jms.model.Identifiable;
import spring.test.jms.model.Ticket;


@Builder
@AllArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class TicketImpl implements Ticket, Identifiable {
    private long id;
    private long eventId;
    private long userId;
    private int place;
    private Ticket.Category category;

    public TicketImpl() {
    }
}
