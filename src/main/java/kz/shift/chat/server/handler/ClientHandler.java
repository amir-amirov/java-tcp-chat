package kz.shift.chat.server.handler;

import kz.shift.chat.common.dto.DtoType;
import kz.shift.chat.common.dto.ErrorDTO;
import kz.shift.chat.common.dto.JoinRequestDTO;
import kz.shift.chat.common.dto.MessageDTO;
import kz.shift.chat.common.json.JsonMapper;
import kz.shift.chat.server.exception.ChatException;
import kz.shift.chat.server.service.chat.ChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(ClientHandler.class);
    private final Socket client;
    private final ChatService chatService;
    private String username = "";

    public ClientHandler(Socket socket, ChatService chatService) {
        client = socket;
        this.chatService = chatService;
    }
    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter out = new PrintWriter(client.getOutputStream(), true)
        ) {
            logger.info("Client connected {}:{}", client.getInetAddress(), client.getPort());

            while (true) {
                String json = in.readLine();
                if (json == null) {
                    return;
                }
                logger.info("Received request: {}", json);
                DtoType dtoType = JsonMapper.getTypeFromJson(json);
                switch (dtoType) {
                    case JOIN -> {
                        JoinRequestDTO joinRequestDTO = JsonMapper.fromJson(json, JoinRequestDTO.class);
                        if (!chatService.canClientJoin(joinRequestDTO)) {
                            logger.info("Client {} NOT joined. Username already taken.", joinRequestDTO.getUsername());
                            ErrorDTO errorDTO = new ErrorDTO("Username already taken.");
                            chatService.sendTo(errorDTO, out);
                            break;
                        }
                        username = joinRequestDTO.getUsername();
                        chatService.joinChat(out, joinRequestDTO);
                        logger.info("Client {} joined the chat.", username);
                    }
                    case MESSAGE -> {
                        MessageDTO messageDTO = JsonMapper.fromJson(json, MessageDTO.class);
                        chatService.broadcast(messageDTO);
                    }
                    case UNKNOWN -> {
                        ErrorDTO errorDTO = new ErrorDTO("Unknown type.");
                        chatService.sendTo(errorDTO, username);
                    }
                }
            }
        } catch (IOException e) {
            logger.warn("Client connection error: {}", e.getMessage());
        } catch (ChatException e) {
            logger.warn("Chat error: {}", e.getMessage());
            ErrorDTO errorDTO = new ErrorDTO(e.getMessage());
            chatService.sendTo(errorDTO, username);
        }
        finally {
            logger.info("Client disconnected {}:{}", client.getInetAddress(), client.getPort());
            chatService.leaveChat(username);
            close();
        }
    }

    private void close() {
        if (!client.isClosed()) {
            try {
                client.close();
            } catch (IOException e) {
                logger.error("Failed to close client connection.", e);
            }
        }
    }
}
