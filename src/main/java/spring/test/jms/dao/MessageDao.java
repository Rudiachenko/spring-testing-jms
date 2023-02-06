package spring.test.jms.dao;

import spring.test.jms.model.Message;

import java.util.List;

public interface MessageDao {
    Message create(Message message);

    List<Message> getAll();
}
