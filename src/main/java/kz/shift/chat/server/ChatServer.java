package kz.shift.chat.server;

import kz.shift.chat.common.config.Config;
import kz.shift.chat.server.db.DatabaseManager;
import kz.shift.chat.server.handler.ClientHandler;
import kz.shift.chat.server.repository.MessageRepository;
import kz.shift.chat.server.repository.MessageRepositoryImpl;
import kz.shift.chat.server.service.chat.ChatService;
import kz.shift.chat.server.service.chat.ChatServiceImpl;
import kz.shift.chat.server.service.message.MessageService;
import kz.shift.chat.server.service.message.MessageServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServer {
    private static final Logger logger = LoggerFactory.getLogger(ChatServer.class);

    private static final int PORT = Config.getInteger("server.port");
    private static final int THREADS = Config.getInteger("server.threads");
    private static final int TIMEOUT = Config.getInteger("server.timeout");

    private static final ExecutorService clientExecutor = Executors.newFixedThreadPool(THREADS);

    public static void main(String[] args) {
        addShutdownHook();
        ChatServer.start();
    }

    public static void start() {
        DataSource dataSource = DatabaseManager.getDataSource();
        MessageRepository messageRepository = new MessageRepositoryImpl(dataSource);

        MessageService messageService = new MessageServiceImpl(messageRepository);
        ChatService chatService = new ChatServiceImpl(messageService);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            logger.info("Chat server started on port {}", PORT);

            while (true) {
                try {
                    Socket client = serverSocket.accept();
                    client.setSoTimeout(TIMEOUT);

                    clientExecutor.submit(new ClientHandler(client, chatService));
                } catch (IOException e) {
                    logger.error("Failed to accept client connection", e);
                }
            }
        } catch (IOException e) {
            logger.error("Fatal error: server crashed", e);
        }
    }

    private static void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Shutting down server...");

            clientExecutor.shutdown();
            DatabaseManager.closePool();

            logger.info("Server shutdown complete");
        }));
    }
}
