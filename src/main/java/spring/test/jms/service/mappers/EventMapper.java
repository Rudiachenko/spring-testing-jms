package spring.test.jms.service.mappers;

import org.springframework.stereotype.Component;
import spring.test.jms.model.Event;
import spring.test.jms.model.dto.EventRequestDto;
import spring.test.jms.model.impl.EventImpl;

@Component
public class EventMapper {
    public Event mapEventRequestDtoToEvent(EventRequestDto eventRequestDto) {
        return EventImpl.builder()
                .title(eventRequestDto.getTitle())
                .date(eventRequestDto.getDate())
                .build();
    }

    public Event mapEventRequestDtoToEvent(EventRequestDto eventRequestDto, long id) {
        return EventImpl.builder()
                .id(id)
                .title(eventRequestDto.getTitle())
                .date(eventRequestDto.getDate())
                .build();
    }
}
