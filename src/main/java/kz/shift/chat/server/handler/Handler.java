package kz.shift.chat.server.handler;

import java.net.Socket;

@FunctionalInterface
public interface Handler {
    void handle(String json, Socket client);
}
