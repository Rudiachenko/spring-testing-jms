package spring.test.jms.service;


import spring.test.jms.model.Message;

import java.util.List;

public interface MessageService {
    Message create(Message message);

    List<Message> getAll();
}
