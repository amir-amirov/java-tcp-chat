package kz.shift.chat.server.handler;

import kz.shift.chat.server.ClientSession;
import kz.shift.chat.server.exception.ChatException;
import kz.shift.chat.server.handler.impl.Dispatcher;
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
    private final Dispatcher dispatcher;
    private ClientSession session;

    public ClientHandler(Socket client, ChatService chatService, Dispatcher dispatcher) {
        this.client = client;
        this.chatService = chatService;
        this.dispatcher = dispatcher;
    }
    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
             PrintWriter out = new PrintWriter(client.getOutputStream(), true)
        ) {
            logger.info("Client connected {}:{}", client.getInetAddress(), client.getPort());

            String remoteAddress = "%s:%s".formatted(client.getInetAddress(), client.getPort());
            this.session = new ClientSession(out, remoteAddress);

            while (true) {
                String json = in.readLine();
                if (json == null) {
                    return;
                }
                dispatcher.handle(json, session);
            }
        } catch (IOException e) {
            logger.warn("Client connection error: {}", e.getMessage());
        } catch (ChatException e) {
            logger.warn("Chat error: {}", e.getMessage());
        }
        finally {
            if (session != null && session.getUsername() != null) {
                chatService.leaveChat(session.getUsername());
            }
            logger.info("Client disconnected {}",
                    session != null ? session.getRemoteAddress() : "unknown");
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
