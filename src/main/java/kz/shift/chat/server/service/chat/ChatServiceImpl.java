package kz.shift.chat.server.service.chat;

import kz.shift.chat.common.dto.DTO;
import kz.shift.chat.common.dto.JoinRequestDTO;
import kz.shift.chat.common.dto.JoinResponseDTO;
import kz.shift.chat.common.dto.MessageDTO;
import kz.shift.chat.common.dto.SystemDTO;
import kz.shift.chat.common.dto.UsersUpdateDTO;
import kz.shift.chat.common.json.JsonMapper;
import kz.shift.chat.server.ClientSession;
import kz.shift.chat.server.mapper.MessageMapper;
import kz.shift.chat.server.model.Message;
import kz.shift.chat.server.service.message.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChatServiceImpl implements ChatService {

    private static final Logger logger = LoggerFactory.getLogger(ChatServiceImpl.class);
    private final MessageService messageService;
    private final Map<String, ClientSession> clients = new ConcurrentHashMap<>();

    public ChatServiceImpl(MessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    public boolean canClientJoin(JoinRequestDTO joinRequestDTO) {
        return clients.get(joinRequestDTO.getUsername()) == null;
    }

    @Override
    public void joinChat(JoinRequestDTO joinRequestDTO, ClientSession session) {
        clients.put(joinRequestDTO.getUsername(), session);
        JoinResponseDTO joinResponseDTO = new JoinResponseDTO(getAllUsers(), getHistory());
        sendTo(joinResponseDTO, session);
        updateUserList();
        SystemDTO systemMessage = new SystemDTO(joinRequestDTO.getUsername() + " joined.");
        broadcast(systemMessage);
    }

    @Override
    public void leaveChat(String username) {
        if (username != null && !username.isEmpty()) {
            clients.remove(username);
            SystemDTO systemMessage = new SystemDTO(username + " left.");
            broadcast(systemMessage);
            updateUserList();
        }
    }

    @Override
    public <T extends DTO> void broadcast(T message) {
        for (ClientSession session : clients.values()) {
            sendTo(message, session);
        }
    }

    @Override
    public <T extends DTO> void sendTo(T dto, ClientSession session) {
        String json = JsonMapper.toJson(dto);
        session.getOut().println(json);
        logger.info("Response sent: {}", json);
    }

    @Override
    public void updateUserList() {
        List<String> users = getAllUsers();
        UsersUpdateDTO usersUpdateDTO = new UsersUpdateDTO(users);
        for (ClientSession session : clients.values()) {
            sendTo(usersUpdateDTO, session);
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
