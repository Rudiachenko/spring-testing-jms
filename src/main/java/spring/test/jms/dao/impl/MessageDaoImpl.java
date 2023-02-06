package spring.test.jms.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import spring.test.jms.dao.MessageDao;
import spring.test.jms.model.Message;
import spring.test.jms.storage.MessageStorage;

import java.util.List;

@Repository
public class MessageDaoImpl implements MessageDao {
    private final MessageStorage messageStorage;

    @Autowired
    public MessageDaoImpl(MessageStorage messageStorage) {
        this.messageStorage = messageStorage;
    }

    @Override
    public Message create(Message message) {
        return messageStorage.add(message);
    }

    @Override
    public List<Message> getAll() {
        return messageStorage.getAll();
    }
}
