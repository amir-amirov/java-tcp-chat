package kz.shift.chat.server.service.chat;

import kz.shift.chat.common.dto.DTO;
import kz.shift.chat.common.dto.JoinRequestDTO;
import kz.shift.chat.server.ClientSession;

public interface ChatService {
    <T extends DTO>  void broadcast(T dto);
    <T extends DTO> void sendTo(T dto, ClientSession session);
    void updateUserList();
    boolean canClientJoin(JoinRequestDTO joinRequestDTO);
    void joinChat(JoinRequestDTO joinRequestDTO, ClientSession session);
    void leaveChat(String username);
}
