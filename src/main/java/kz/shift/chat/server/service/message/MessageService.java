package kz.shift.chat.server.service.message;

import kz.shift.chat.server.model.Message;

import java.util.List;

public interface MessageService {
    Message createMessage(Message message);
    List<Message> getAll();
}
