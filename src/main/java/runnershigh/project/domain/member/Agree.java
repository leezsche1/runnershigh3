package runnershigh.project.domain.member;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Agree {


    private int firstReqAgree;
    private int secondReqAgree;

    private int firstSelAgree;
    private int secondSelAgree;
    private int thirdSelAgree;


    protected Agree() {}


    public Agree(int firstReqAgree, int secondReqAgree, int firstSelAgree, int secondSelAgree, int thirdSelAgree) {
        this.firstReqAgree = firstReqAgree;
        this.secondReqAgree = secondReqAgree;
        this.firstSelAgree = firstSelAgree;
        this.secondSelAgree = secondSelAgree;
        this.thirdSelAgree = thirdSelAgree;
    }
}
