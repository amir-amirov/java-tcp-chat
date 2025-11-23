package kz.shift.chat.server.service.message;

import kz.shift.chat.server.model.Message;
import kz.shift.chat.server.repository.MessageRepository;

import java.util.List;

public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;

    public MessageServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public Message createMessage(Message message) {
        return messageRepository.save(message);
    }

    @Override
    public List<Message> getAll() {
        return messageRepository.findAll();
    }
}
