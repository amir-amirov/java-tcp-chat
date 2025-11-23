package kz.shift.chat.client;

import kz.shift.chat.common.dto.MessageDTO;
import kz.shift.chat.common.dto.SystemDTO;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;

public class ChatPanel extends JPanel {
    private JTextArea chatArea;
    private JTextField inputField;
    private ChatClient chatClient;

    public ChatPanel(ChatClient chatClient) {
        this.chatClient = chatClient;
        setLayout(new BorderLayout());
        initializeComponents();
    }

    private void initializeComponents() {
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane chatScroll = new JScrollPane(chatArea);
        add(chatScroll, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputField = new JTextField();
        inputField.addActionListener(e -> chatClient.sendMessage());
        inputPanel.add(inputField, BorderLayout.CENTER);

        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(e -> chatClient.sendMessage());
        inputPanel.add(sendButton, BorderLayout.EAST);

        add(inputPanel, BorderLayout.SOUTH);
    }

    public void appendMessage(MessageDTO message) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        SwingUtilities.invokeLater(() -> {
            chatArea.append("[" + message.getCreatedAt().format(dtf) + "] " + message.getSender() + ": " + message.getText() + "\n");
            chatArea.setCaretPosition(chatArea.getDocument().getLength());
        });
    }

    public void appendSystem(SystemDTO system) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        SwingUtilities.invokeLater(() -> {
            chatArea.append("[" + system.getCreatedAt().format(dtf) + "] System: " + system.getText() + "\n");
            chatArea.setCaretPosition(chatArea.getDocument().getLength());
        });
    }

    public void appendText(String text) {
        SwingUtilities.invokeLater(() -> {
            chatArea.append(text);
            chatArea.setCaretPosition(chatArea.getDocument().getLength());
        });
    }

    public void clearChat() {
        SwingUtilities.invokeLater(() -> {
            chatArea.setText("");
        });
    }

    public String getInputText() {
        return inputField.getText();
    }

    public void clearInput() {
        inputField.setText("");
    }
}