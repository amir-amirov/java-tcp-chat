package kz.shift.chat.common.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class ErrorDTO extends DTO {
    private String message;

    public ErrorDTO(String message) {
        super(DtoType.ERROR);
        this.message = message;
    }
}
