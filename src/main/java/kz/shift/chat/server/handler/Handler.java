package kz.shift.chat.server.handler;

import kz.shift.chat.server.ClientSession;

public interface Handler {
    void handle(String json, ClientSession session);
}
