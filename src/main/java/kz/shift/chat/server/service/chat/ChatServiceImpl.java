package kz.shift.chat.server.service.chat;

import kz.shift.chat.common.dto.DTO;
import kz.shift.chat.common.dto.JoinRequestDTO;
import kz.shift.chat.common.dto.JoinResponseDTO;
import kz.shift.chat.common.dto.MessageDTO;
import kz.shift.chat.common.dto.SystemDTO;
import kz.shift.chat.common.dto.UsersUpdateDTO;
import kz.shift.chat.common.json.JsonMapper;
import kz.shift.chat.server.mapper.MessageMapper;
import kz.shift.chat.server.model.Message;
import kz.shift.chat.server.service.message.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChatServiceImpl implements ChatService {

    private static final Logger logger = LoggerFactory.getLogger(ChatServiceImpl.class);
    private final MessageService messageService;
    private final Map<String, PrintWriter> clients = new ConcurrentHashMap<>();

    public ChatServiceImpl(MessageService messageService) {
        this.messageService = messageService;
    }

    public boolean canClientJoin(JoinRequestDTO joinRequestDTO) {
        return clients.get(joinRequestDTO.getUsername()) == null;
    }

    public void joinChat(PrintWriter out, JoinRequestDTO joinRequestDTO) {
        clients.put(joinRequestDTO.getUsername(), out);
        JoinResponseDTO joinResponseDTO = new JoinResponseDTO(getAllUsers(), getHistory());
        sendTo(joinResponseDTO, out);
        updateUserList();
        SystemDTO systemMessage = new SystemDTO(joinRequestDTO.getUsername() + " joined.");
        broadcast(systemMessage);
    }

    public void leaveChat(String username) {
        if (username != null && !username.isEmpty()) {
            clients.remove(username);
            SystemDTO systemMessage = new SystemDTO(username + " left.");
            broadcast(systemMessage);
            updateUserList();
        }
    }

    public <T extends DTO> void broadcast(T message) {
        for (PrintWriter out : clients.values()) {
            sendTo(message, out);
        }
    }

    public <T extends DTO> void sendTo(T dto, PrintWriter out) {
        if (dto instanceof MessageDTO messageDTO) {
            messageService.createMessage(MessageMapper.toMessage(messageDTO));
        }
        String json = JsonMapper.toJson(dto);
        out.println(json);
        logger.info("Response sent: {}", json);
    }

    public <T extends DTO> void sendTo(T dto, String username) {
        if (username != null && !username.isEmpty()) {
            sendTo(dto, clients.get(username));
        }
    }

    public void updateUserList() {
        List<String> users = getAllUsers();
        UsersUpdateDTO usersUpdateDTO = new UsersUpdateDTO(users);
        for (PrintWriter out : clients.values()) {
            sendTo(usersUpdateDTO, out);
        }
    }

    private List<MessageDTO> getHistory() {
        List<MessageDTO> history = new ArrayList<>();
        List<Message> messages = messageService.getAll();

        for (Message message : messages) {
            history.add(MessageMapper.toMessageDTO(message));
        }

        return history;
    }

    private List<String> getAllUsers() {
        return clients.keySet().stream().toList();
    }

}
