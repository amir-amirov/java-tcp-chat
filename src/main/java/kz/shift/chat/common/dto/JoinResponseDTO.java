package kz.shift.chat.common.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@NoArgsConstructor
@Getter
@ToString
public class JoinResponseDTO extends DTO {
    private List<String> users;
    private List<MessageDTO> history;

    public JoinResponseDTO(List<String> users, List<MessageDTO> history) {
        super(DtoType.JOIN);
        this.users = users;
        this.history = history;
    }
}
