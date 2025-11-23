package kz.shift.chat.server.repository;

import kz.shift.chat.server.model.Message;

import java.util.List;

public interface MessageRepository {
    Message save(Message message);
    List<Message> findAll();
}
