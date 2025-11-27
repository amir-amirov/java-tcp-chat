package kz.shift.chat.server.handler.impl;

import kz.shift.chat.common.dto.DtoType;
import kz.shift.chat.common.json.JsonMapper;
import kz.shift.chat.server.ClientSession;
import kz.shift.chat.server.handler.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class Dispatcher implements Handler {
    private static final Logger logger = LoggerFactory.getLogger(Dispatcher.class);
    private final Map<DtoType, Handler> handlers = new HashMap<>();

    public void register(DtoType type, Handler handler) {
        handlers.put(type, handler);
    }

    @Override
    public void handle(String json, ClientSession session) {
        logger.info("Received request: {}", json);
        DtoType type = JsonMapper.getTypeFromJson(json);
        handlers.get(type).handle(json, session);
    }
}
