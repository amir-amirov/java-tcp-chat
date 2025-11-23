package kz.shift.chat.server.service.chat;

import kz.shift.chat.common.dto.DTO;
import kz.shift.chat.common.dto.JoinRequestDTO;

import java.io.PrintWriter;

public interface ChatService {
    <T extends DTO>  void broadcast(T dto);
    <T extends DTO> void sendTo(T dto, PrintWriter out);
    <T extends DTO> void sendTo(T dto, String name);
    void updateUserList();
    boolean canClientJoin(JoinRequestDTO joinRequestDTO);
    void joinChat(PrintWriter out, JoinRequestDTO joinRequestDTO);
    void leaveChat(String username);
}
