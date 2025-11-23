package kz.shift.chat.common.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public abstract class DTO {
    private DtoType type;

    protected DTO(DtoType type) {
        this.type = type;
    }
}
