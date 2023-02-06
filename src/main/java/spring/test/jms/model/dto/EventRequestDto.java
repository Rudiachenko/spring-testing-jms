package spring.test.jms.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class EventRequestDto {
    private String title;
    private Date date;
}
