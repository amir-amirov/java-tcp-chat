package kz.shift.chat.common.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@ToString
public class SystemDTO extends DTO {
    private String text;
    private LocalDateTime createdAt;

    public SystemDTO(String text) {
        super(DtoType.SYSTEM);
        this.text = text;
        this.createdAt = LocalDateTime.now();
    }
}
