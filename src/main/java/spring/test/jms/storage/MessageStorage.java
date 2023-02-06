package spring.test.jms.storage;

import spring.test.jms.model.Message;

import java.util.List;

public interface MessageStorage {
    Message add(Message message);

    List<Message> getAll();
}
