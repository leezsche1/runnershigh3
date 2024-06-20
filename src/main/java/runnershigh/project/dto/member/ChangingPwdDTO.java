package runnershigh.project.dto.member;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangingPwdDTO {

    @NotNull(message = "이메일값은 필수입니다.")
    private String email;

    @NotNull(message = "비밀번호값은 필수입니다.")
    private String password;
}
