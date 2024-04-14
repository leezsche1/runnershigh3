package runnershigh.project.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberIdCheckDTO {

    @NotNull(message = "email값은 필수입니다.")
    private String email;

}
