package spring.test.jms.storage.impl;

import org.springframework.stereotype.Component;
import spring.test.jms.model.Message;
import spring.test.jms.storage.MessageStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MessageStorageImpl implements MessageStorage {
    private final Map<Long, Message> messageMapStorageMap = new HashMap<>();

    @Override
    public Message add(Message message) {
        return messageMapStorageMap.put(message.getId(), message);
    }

    @Override
    public List<Message> getAll() {
        return new ArrayList<>(messageMapStorageMap.values());
    }
}
