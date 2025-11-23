//package kz.shift.chat.server.handler;
//
//import kz.shift.chat.common.dto.DtoType;
//import kz.shift.chat.common.dto.ErrorDTO;
//import kz.shift.chat.common.json.JsonMapper;
//
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.net.Socket;
//import java.util.HashMap;
//import java.util.Map;
//
//public class HandleDispatcher {
//    private final Map<DtoType, Handler> handlers = new HashMap<>();
//
//    public void register(DtoType type, Handler handler) {
//        handlers.put(type, handler);
//    }
//
//    public Handler get(DtoType type) {
//        return handlers.getOrDefault(type, this::handleUnknown);
//    }
//
//    private void handleUnknown(String json, Socket client) {
//        if (client != null && !client.isClosed()) {
//            try {
//                new PrintWriter(client.getOutputStream()).println(json);
//            } catch (IOException e) {
//
//            }
//        }
//    }
//}
