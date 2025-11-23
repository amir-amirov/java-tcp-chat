package kz.shift.chat.common.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import kz.shift.chat.common.dto.DtoType;
import kz.shift.chat.server.exception.ChatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonMapper {
    private static final Logger logger = LoggerFactory.getLogger(JsonMapper.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        // Без модуля не работает сериализация LocalDateTime
        mapper.registerModule(new JavaTimeModule());
    }

    public static String toJson(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            logger.error("Failed to serialize message {}", obj, e);
            throw new ChatException("Failed to serialize message");
        }
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            logger.error("Failed to deserialize json {} into {} class", json, clazz.getSimpleName(), e);
            throw new ChatException("Failed to deserialize json");
        }
    }

    public static DtoType getTypeFromJson(String json) {
        try {
            JsonNode rootNode = mapper.readTree(json);
            String type = rootNode.get("type").asText();
            return DtoType.valueOf(type);
        } catch (IllegalArgumentException | JsonProcessingException e) {
            logger.error("Invalid json request. Failed to get type from json {}", json, e);
            return DtoType.UNKNOWN;
        }
    }
}
