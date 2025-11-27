package kz.shift.chat.server.mapper;

import kz.shift.chat.common.dto.MessageDTO;
import kz.shift.chat.server.model.Message;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class MessageMapperTest {

    private static final Long ID = 1L;
    private static final String SENDER = "john";
    private static final String TEXT = "Привет, мир!";
    private static final LocalDateTime CREATED_AT = LocalDateTime.of(2025, 4, 1, 12, 0);

    @Test
    @DisplayName("toMessageDTO() корректно маппит все поля из сущности в DTO")
    void toMessageDTO_mapsAllFieldsCorrectly() {
        // given
        Message message = new Message();
        message.setId(ID);
        message.setSender(SENDER);
        message.setText(TEXT);
        message.setCreatedAt(CREATED_AT);

        // when
        MessageDTO dto = MessageMapper.toMessageDTO(message);

        // then
        assertThat(dto)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", ID)
                .hasFieldOrPropertyWithValue("sender", SENDER)
                .hasFieldOrPropertyWithValue("text", TEXT)
                .hasFieldOrPropertyWithValue("createdAt", CREATED_AT);
    }

    @Test
    @DisplayName("toMessage() маппит sender и text, не трогает id и createdAt")
    void toMessage_mapsSenderAndText_onlyIdAndCreatedAtRemainNull() {
        // given
        MessageDTO dto = new MessageDTO(null, SENDER, TEXT, null);

        // when
        Message entity = MessageMapper.toMessage(dto);

        // then
        assertThat(entity)
                .isNotNull()
                .hasFieldOrPropertyWithValue("sender", SENDER)
                .hasFieldOrPropertyWithValue("text", TEXT)
                .hasFieldOrPropertyWithValue("id", null)     // должен остаться null
                .hasFieldOrPropertyWithValue("createdAt", null);
    }

    @Test
    @DisplayName("toMessageDTO() работает с null полями — не падает")
    void toMessageDTO_handlesNullFieldsGracefully() {
        // given
        Message message = new Message();

        // when
        MessageDTO dto = MessageMapper.toMessageDTO(message);

        // then
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isNull();
        assertThat(dto.getSender()).isNull();
        assertThat(dto.getText()).isNull();
        assertThat(dto.getCreatedAt()).isNull();
    }
}