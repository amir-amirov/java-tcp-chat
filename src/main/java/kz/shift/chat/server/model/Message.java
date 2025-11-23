package kz.shift.chat.server.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class Message {
    private Long id;
    private String sender;
    private String text;
    private LocalDateTime createdAt;
}
