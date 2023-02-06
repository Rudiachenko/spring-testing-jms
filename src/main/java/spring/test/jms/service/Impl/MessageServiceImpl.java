package spring.test.jms.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.test.jms.dao.MessageDao;
import spring.test.jms.model.Message;
import spring.test.jms.model.impl.MessageImpl;
import spring.test.jms.service.MessageService;
import spring.test.jms.util.IdGenerator;

import java.util.List;

@Slf4j
@Service
public class MessageServiceImpl implements MessageService {
    private final MessageDao messageDao;
    private final IdGenerator idGenerator;

    @Autowired
    public MessageServiceImpl(MessageDao messageDao, IdGenerator idGenerator) {
        this.messageDao = messageDao;
        this.idGenerator = idGenerator;
    }

    @Override
    public Message create(Message message) {
        long generatedId = idGenerator.generateId(MessageImpl.class);
        log.info("Adding message to DB. Message id: " + generatedId);
        message.setId(generatedId);
        Message messageFromDb = messageDao.create(message);
        log.info("Message was successfully added. Message id: " + generatedId);
        return messageFromDb;
    }

    @Override
    public List<Message> getAll() {
        return messageDao.getAll();
    }
}
