package kz.shift.chat.server.handler.impl;

import kz.shift.chat.common.dto.ErrorDTO;
import kz.shift.chat.server.ClientSession;
import kz.shift.chat.server.handler.Handler;
import kz.shift.chat.server.service.chat.ChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnknownHandler implements Handler {

    private static final Logger logger = LoggerFactory.getLogger(UnknownHandler.class);
    private final ChatService chatService;

    public UnknownHandler(ChatService chatService) {
        this.chatService = chatService;
    }

    @Override
    public void handle(String json, ClientSession session) {
        ErrorDTO errorDTO = new ErrorDTO("Unknown type.");
        chatService.sendTo(errorDTO, session);
    }
}
