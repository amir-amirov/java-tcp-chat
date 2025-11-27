package kz.shift.chat.server.mapper;

import kz.shift.chat.common.dto.MessageDTO;
import kz.shift.chat.server.model.Message;

public final class MessageMapper {
    private MessageMapper() {}

    public static MessageDTO toMessageDTO(Message message) {
        return new MessageDTO(
                message.getId(),
                message.getSender(),
                message.getText(),
                message.getCreatedAt()
        );
    }

    public static Message toMessage(MessageDTO messageDTO) {
        Message message = new Message();
        message.setSender(messageDTO.getSender());
        message.setText(messageDTO.getText());
        return message;
    }

}
