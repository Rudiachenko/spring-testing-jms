package spring.test.jms.model.impl;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import spring.test.jms.model.Event;
import spring.test.jms.model.Identifiable;

import java.util.Date;

@Builder
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class EventImpl implements Event, Identifiable {
    private long id;
    private String title;
    private Date date;
}
