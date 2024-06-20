package runnershigh.project.domain.member;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class MemberInfo {

    private String nickName;
    private String gender;

    private String height;
    private String weight;

    protected MemberInfo() {
    }

    public MemberInfo(String nickName, String gender, String height, String weight) {
        this.nickName = nickName;
        this.gender = gender;
        this.height = height;
        this.weight = weight;
    }
}
