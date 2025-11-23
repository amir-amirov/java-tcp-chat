package kz.shift.chat.common.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class JoinRequestDTO extends DTO {
    private String username;

    public JoinRequestDTO(String username) {
        super(DtoType.JOIN);
        this.username = username;
    }
}
