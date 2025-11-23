package kz.shift.chat.common.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class MessageDTO extends DTO {
    private Long id;
    private String sender;
    private String text;
    private LocalDateTime createdAt;

    public MessageDTO(Long id, String sender, String text, LocalDateTime createdAt) {
        super(DtoType.MESSAGE);
        this.id = id;
        this.sender = sender;
        this.text = text;
        this.createdAt = createdAt;
    }
}
