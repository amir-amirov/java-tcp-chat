package kz.shift.chat.server.handler.impl;

import kz.shift.chat.common.dto.ErrorDTO;
import kz.shift.chat.common.dto.JoinRequestDTO;
import kz.shift.chat.common.json.JsonMapper;
import kz.shift.chat.server.ClientSession;
import kz.shift.chat.server.handler.Handler;
import kz.shift.chat.server.service.chat.ChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JoinHandler implements Handler {

    private static final Logger logger = LoggerFactory.getLogger(JoinHandler.class);
    private final ChatService chatService;

    public JoinHandler(ChatService chatService) {
        this.chatService = chatService;
    }

    @Override
    public void handle(String json, ClientSession session) {
        JoinRequestDTO joinRequestDTO = JsonMapper.fromJson(json, JoinRequestDTO.class);
        if (!chatService.canClientJoin(joinRequestDTO)) {
            logger.info("Client {} NOT joined. Username already taken.", joinRequestDTO.getUsername());
            ErrorDTO errorDTO = new ErrorDTO("Username already taken.");
            chatService.sendTo(errorDTO, session);
            return;
        }
        chatService.joinChat(joinRequestDTO, session);
        session.setUsername(joinRequestDTO.getUsername());
        logger.info("Client {} joined the chat.", session.getUsername());
    }
}
