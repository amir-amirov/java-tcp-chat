package kz.shift.chat.server.service.message;

import kz.shift.chat.server.model.Message;
import kz.shift.chat.server.repository.MessageRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("MessageServiceImpl")
class MessageServiceImplTest {

    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private MessageServiceImpl messageService;

    @Test
    @DisplayName("createMessage() сохраняет сообщение и возвращает его")
    void createMessage_savesAndReturnsMessage() {
        // given
        Message message = new Message();
        message.setSender("alice");
        message.setText("hello");

        Message savedMessage = new Message();
        savedMessage.setId(1L);
        savedMessage.setSender("alice");
        savedMessage.setText("hello");

        when(messageRepository.save(any(Message.class))).thenReturn(savedMessage);

        // when
        Message result = messageService.createMessage(message);

        // then
        assertThat(result)
                .isNotNull()
                .isSameAs(savedMessage)
                .hasFieldOrPropertyWithValue("id", 1L);

        verify(messageRepository).save(message);
    }

    @Test
    @DisplayName("getAll() возвращает все сообщения из репозитория")
    void getAll_returnsAllMessages() {
        // given
        Message msg1 = new Message(); msg1.setId(1L); msg1.setText("hi");
        Message msg2 = new Message(); msg2.setId(2L); msg2.setText("bye");

        when(messageRepository.findAll()).thenReturn(List.of(msg1, msg2));

        // when
        List<Message> result = messageService.getAll();

        // then
        assertThat(result)
                .hasSize(2)
                .containsExactly(msg1, msg2);

        verify(messageRepository).findAll();
    }
}