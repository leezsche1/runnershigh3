package runnershigh.project.domain.member;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import runnershigh.project.domain.RegEntity;
import runnershigh.project.domain.running.RunningRecord;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Member extends RegEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "name")
    private String name;

    @Column(name = "password")
    private String memberPwd;

    @Column(name = "phoneNumber")
    private String memberPhone;

    @Column(name = "birth")
    private String birth;

    @Embedded
    private Agree agree;

    @Embedded
    private MemberInfo memberInfo;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    public List<MemberRole> memberRoles = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    public List<RunningRecord> runningRecords = new ArrayList<>();

    public void addMemberRole(MemberRole memberRole) {
        memberRoles.add(memberRole);
    }
}
