package spring.test.jms.service.mappers;

import org.springframework.stereotype.Component;
import spring.test.jms.model.User;
import spring.test.jms.model.dto.UserRequestDto;
import spring.test.jms.model.impl.UserImpl;

@Component
public class UserMapper {

    public User mapUserRequestDtoToUser(UserRequestDto userRequestDto) {
        return UserImpl.builder()
                .name(userRequestDto.getName())
                .email(userRequestDto.getEmail())
                .build();
    }

    public User mapUserRequestDtoToUser(UserRequestDto userRequestDto, long id) {
        return UserImpl.builder()
                .id(id)
                .name(userRequestDto.getName())
                .email(userRequestDto.getEmail())
                .build();
    }
}
