package spring.test.jms.model.impl;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import spring.test.jms.model.Identifiable;
import spring.test.jms.model.User;

@Builder
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class UserImpl implements User, Identifiable {
    private long id;
    private String name;
    private String email;
}
