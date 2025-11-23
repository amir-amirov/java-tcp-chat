package kz.shift.chat.common.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@NoArgsConstructor
@Getter
@ToString
public class UsersUpdateDTO extends DTO {
    private List<String> users;

    public UsersUpdateDTO(List<String> users) {
        super(DtoType.USERS_UPDATE);
        this.users = users;
    }
}
