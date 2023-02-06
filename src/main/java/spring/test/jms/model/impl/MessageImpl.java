package spring.test.jms.model.impl;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import spring.test.jms.model.Identifiable;
import spring.test.jms.model.Message;

@Builder
@Getter
@Setter
public class MessageImpl implements Message, Identifiable {
    private long id;
    private String content;
}
