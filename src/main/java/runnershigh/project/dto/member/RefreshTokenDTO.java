package runnershigh.project.dto.member;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshTokenDTO {

    private String memberId;
    private String memberEmail;
    private String refreshToken;

}
