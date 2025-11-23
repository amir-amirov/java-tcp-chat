package kz.shift.chat.client;

import kz.shift.chat.common.dto.DtoType;
import kz.shift.chat.common.dto.ErrorDTO;
import kz.shift.chat.common.dto.JoinRequestDTO;
import kz.shift.chat.common.dto.JoinResponseDTO;
import kz.shift.chat.common.dto.MessageDTO;
import kz.shift.chat.common.dto.SystemDTO;
import kz.shift.chat.common.dto.UsersUpdateDTO;
import kz.shift.chat.common.json.JsonMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;

public class NetworkClient {
    private String host;
    private int port;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private boolean connected = false;
    private ChatClient chatClient;

    public NetworkClient(String host, int port, ChatClient chatClient) {
        this.host = host;
        this.port = port;
        this.chatClient = chatClient;
    }

    public boolean connect() {
        try {
            socket = new Socket(host, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            connected = true;
            new Thread(this::readMessages).start();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public void disconnect() {
        if (connected) {
            connected = false;
            try {
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                }
            } catch (IOException ignored) {
            }
        }
    }

    public void sendJoinRequest(String username) {
        JoinRequestDTO joinRequest = new JoinRequestDTO(username);
        String json = JsonMapper.toJson(joinRequest);
        out.println(json);
    }

    public void sendMessage(String username, String text) {
        if (!text.isEmpty() && connected && chatClient.isJoined()) {
            MessageDTO message = new MessageDTO(null, username, text, LocalDateTime.now());
            String json = JsonMapper.toJson(message);
            out.println(json);
        }
    }

    private void readMessages() {
        while (connected) {
            try {
                String json = in.readLine();
                if (json == null) {
                    chatClient.handleDisconnect();
                    return;
                }

                DtoType type = JsonMapper.getTypeFromJson(json);
                switch (type) {
                    case JOIN -> {
                        JoinResponseDTO response = JsonMapper.fromJson(json, JoinResponseDTO.class);
                        chatClient.handleJoinResponse(response);
                    }
                    case MESSAGE -> {
                        MessageDTO message = JsonMapper.fromJson(json, MessageDTO.class);
                        chatClient.handleMessage(message);
                    }
                    case SYSTEM -> {
                        SystemDTO system = JsonMapper.fromJson(json, SystemDTO.class);
                        chatClient.handleSystem(system);
                    }
                    case USERS_UPDATE -> {
                        UsersUpdateDTO update = JsonMapper.fromJson(json, UsersUpdateDTO.class);
                        chatClient.handleUsersUpdate(update);
                    }
                    case ERROR -> {
                        ErrorDTO error = JsonMapper.fromJson(json, ErrorDTO.class);
                        chatClient.handleError(error);
                    }
                }
            } catch (IOException e) {
                chatClient.handleDisconnect();
                return;
            }
        }
    }

    public boolean isConnected() {
        return connected;
    }
}