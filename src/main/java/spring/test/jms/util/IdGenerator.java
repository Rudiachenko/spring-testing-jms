package spring.test.jms.util;

import org.springframework.stereotype.Component;
import spring.test.jms.model.impl.EventImpl;
import spring.test.jms.model.impl.MessageImpl;
import spring.test.jms.model.impl.TicketImpl;
import spring.test.jms.model.impl.UserImpl;

@Component
public class IdGenerator {
    private static long EVENT_ID = 0;
    private static long TICKET_ID = 0;
    private static long USER_ID = 0;
    private static long MESSAGE_ID = 0;
    private static final long DEFAULT_ID = 1;

    public long generateId(Class<?> clazz) {
        if (clazz.equals(EventImpl.class)) {
            EVENT_ID++;
            return EVENT_ID;
        } else if (clazz.equals(TicketImpl.class)) {
            TICKET_ID++;
            return TICKET_ID;
        } else if (clazz.equals(UserImpl.class)) {
            USER_ID++;
            return USER_ID;
        } else if (clazz.equals(MessageImpl.class)) {
            MESSAGE_ID++;
            return MESSAGE_ID;
        }
        return DEFAULT_ID;
    }
}
