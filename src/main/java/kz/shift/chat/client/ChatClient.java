package kz.shift.chat.client;

import kz.shift.chat.common.dto.ErrorDTO;
import kz.shift.chat.common.dto.JoinResponseDTO;
import kz.shift.chat.common.dto.MessageDTO;
import kz.shift.chat.common.dto.SystemDTO;
import kz.shift.chat.common.dto.UsersUpdateDTO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class ChatClient extends JFrame {
    private String username;
    private String host;
    private int port;
    private NetworkClient networkClient;
    private ChatPanel chatPanel;
    private UserListPanel userListPanel;
    private boolean isJoined = false;
    private boolean isReconnect = false;

    public ChatClient(String host, int port, String username) {
        this.host = host;
        this.port = port;
        this.username = username;

        initializeFrame();
        initializeComponents();
    }

    private void initializeFrame() {
        setTitle("Shift chat - " + username);
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                disconnect();
                System.exit(0);
            }
        });
    }

    private void initializeComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        chatPanel = new ChatPanel(this);
        mainPanel.add(chatPanel, BorderLayout.CENTER);

        userListPanel = new UserListPanel();
        mainPanel.add(userListPanel, BorderLayout.EAST);

        add(mainPanel);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String host = JOptionPane.showInputDialog("Enter server host:");
            if (host == null) {
                System.exit(0);
            }

            String portStr = JOptionPane.showInputDialog("Enter server port:");
            if (portStr == null) {
                System.exit(0);
            }
            int port = Integer.parseInt(portStr);

            String username = JOptionPane.showInputDialog("Enter your username:");
            if (username == null) {
                System.exit(0);
            }

            ChatClient client = new ChatClient(host, port, username);
            if (!client.connect()) {
                JOptionPane.showMessageDialog(client, "Failed to connect to server.");
                System.exit(1);
            }
        });
    }

    public boolean connect() {
        networkClient = new NetworkClient(host, port, this);
        boolean connected = networkClient.connect();
        if (connected) {
            networkClient.sendJoinRequest(username);
        }
        return connected;
    }

    public void disconnect() {
        if (networkClient != null) {
            networkClient.disconnect();
        }
        isJoined = false;
    }

    public void sendMessage() {
        String text = chatPanel.getInputText().trim();
        if (!text.isEmpty()) {
            networkClient.sendMessage(username, text);
            chatPanel.clearInput();
        }
    }

    public void handleJoinResponse(JoinResponseDTO response) {
        if (isReconnect) {
            chatPanel.clearChat();
            isReconnect = false;
        }
        userListPanel.updateUsers(response.getUsers());
        appendHistory(response.getHistory());
        isJoined = true;
    }

    public void handleMessage(MessageDTO message) {
        chatPanel.appendMessage(message);
    }

    public void handleSystem(SystemDTO system) {
        chatPanel.appendSystem(system);
    }

    public void handleUsersUpdate(UsersUpdateDTO update) {
        userListPanel.updateUsers(update.getUsers());
    }

    public void handleError(ErrorDTO error) {
        SwingUtilities.invokeLater(() -> {
            if (!isJoined && error.getMessage().contains("Username already taken")) {
                String newUsername = JOptionPane.showInputDialog(this, error.getMessage() + "\nEnter new username:");
                if (newUsername != null && !newUsername.trim().isEmpty()) {
                    username = newUsername.trim();
                    setTitle("Chat - " + username);
                    networkClient.sendJoinRequest(username);
                } else {
                    disconnect();
                    System.exit(0);
                }
            } else {
                chatPanel.appendText("Error: " + error.getMessage() + "\n");
            }
        });
    }

    public void handleDisconnect() {
        if (networkClient.isConnected()) {
            chatPanel.appendText("Disconnected from server. Trying to reconnect...\n");
            disconnect();
            isReconnect = true;
            while (true) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ignored) {
                }
                if (connect()) {
                    break;
                }
            }
        }
    }

    private void appendHistory(List<MessageDTO> history) {
        SwingUtilities.invokeLater(() -> {
            for (MessageDTO msg : history) {
                chatPanel.appendMessage(msg);
            }
        });
    }

    public boolean isJoined() {
        return isJoined;
    }
}