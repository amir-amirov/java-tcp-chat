package kz.shift.chat.server.mapper;

import kz.shift.chat.common.dto.MessageDTO;
import kz.shift.chat.server.model.Message;

public final class MessageMapper {
    private MessageMapper() {}

    public static MessageDTO toMessageDTO(Message message) {
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setId(message.getId());
        messageDTO.setSender(message.getSender());
        messageDTO.setText(message.getText());
        messageDTO.setCreatedAt(message.getCreatedAt());
        return messageDTO;
    }

    public static Message toMessage(MessageDTO messageDTO) {
        Message message = new Message();
        message.setSender(messageDTO.getSender());
        message.setText(messageDTO.getText());
        return message;
    }

}
