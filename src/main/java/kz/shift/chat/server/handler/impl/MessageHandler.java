package kz.shift.chat.server.handler.impl;

import kz.shift.chat.common.dto.ErrorDTO;
import kz.shift.chat.common.dto.MessageDTO;
import kz.shift.chat.common.json.JsonMapper;
import kz.shift.chat.server.ClientSession;
import kz.shift.chat.server.handler.Handler;
import kz.shift.chat.server.mapper.MessageMapper;
import kz.shift.chat.server.model.Message;
import kz.shift.chat.server.service.chat.ChatService;
import kz.shift.chat.server.service.message.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageHandler implements Handler {

    private static final Logger logger = LoggerFactory.getLogger(MessageHandler.class);
    private final ChatService chatService;
    private final MessageService messageService;

    public MessageHandler(ChatService chatService, MessageService messageService) {
        this.chatService = chatService;
        this.messageService = messageService;
    }

    @Override
    public void handle(String json, ClientSession session) {
        if (session.getUsername() != null) {
            MessageDTO messageDTO = JsonMapper.fromJson(json, MessageDTO.class);
            Message message = MessageMapper.toMessage(messageDTO);
            message = messageService.createMessage(message);
            chatService.broadcast(MessageMapper.toMessageDTO(message));
        } else {
            ErrorDTO errorDTO = new ErrorDTO("Unauthorized");
            chatService.sendTo(errorDTO, session);
        }
    }
}