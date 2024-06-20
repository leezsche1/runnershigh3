package runnershigh.project.dto.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
//@AllArgsConstructor
public class MemberJoinResDTO {

    private Long id;
    private String email;
    private String memberPwd;
    private String role;
    private String memberPhone;

}
