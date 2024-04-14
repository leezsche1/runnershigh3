package runnershigh.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import runnershigh.project.domain.Member;
import runnershigh.project.domain.MemberRole;
import runnershigh.project.domain.Role;
import runnershigh.project.repository.MemberRepository;
import runnershigh.project.repository.RoleRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;


    public boolean checkEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    @Transactional
    public Member join(Member member) {
        Optional<Role> roleUser = roleRepository.findByRole("ROLE_USER");
        MemberRole memberRole = new MemberRole();
        memberRole.setMember(member);
        memberRole.setRole(roleUser.get());
        member.addMemberRole(memberRole);
        return memberRepository.save(member);
    }

    public Optional<Member> findByEmail(String memberEmail) {
        return memberRepository.findByEmail(memberEmail);
    }
}
