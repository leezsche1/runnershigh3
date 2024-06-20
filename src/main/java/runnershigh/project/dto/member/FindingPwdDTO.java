package runnershigh.project.dto.member;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FindingPwdDTO {

    @NotNull(message = "이메일주소는 필수입니다.")
    private String email;

    @NotNull(message = "이름값은 필수입니다.")
    private String name;

    @NotNull(message = "인증번호는 필수입니다.")
    private String authNumber;

    @NotNull(message = "번호는 필수입니다.")
    private String phoneNumber;



}
