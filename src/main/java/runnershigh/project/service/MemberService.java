package runnershigh.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import runnershigh.project.domain.member.Member;
import runnershigh.project.domain.member.MemberRole;
import runnershigh.project.domain.member.Role;
import runnershigh.project.dto.member.ChangingPwdDTO;
import runnershigh.project.dto.member.FindingIdDTO;
import runnershigh.project.dto.member.FindingPwdDTO;
import runnershigh.project.handler.CustomExceptionCode;
import runnershigh.project.handler.CustomValidationException;
import runnershigh.project.repository.MemberRepository;
import runnershigh.project.repository.RoleRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

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

    public Member findByName(FindingIdDTO findingIdDTO) {

        Optional<Member> findingMember = memberRepository.findByName(findingIdDTO.getName(), findingIdDTO.getPhoneNumber());
        if (findingMember.isEmpty()) {
            throw new CustomValidationException("회원이 존재하지 않습니다.", CustomExceptionCode.NO_EXISTING_ID);
        }

        return findingMember.get();

    }

    public Member findByEmailAndName(FindingPwdDTO findingPwdDTO) {
        Optional<Member> findingMember = memberRepository.findByEmailAndName(findingPwdDTO.getEmail(), findingPwdDTO.getName(), findingPwdDTO.getPhoneNumber());
        if (findingMember.isEmpty()) {
            throw new CustomValidationException("회원이 존재하지 않습니다.", CustomExceptionCode.NO_EXISTING_ID);
        }

        return findingMember.get();
    }

    @Transactional
    public void changingPWd(ChangingPwdDTO changingPwdDTO) {

        Optional<Member> byEmail = memberRepository.findByEmail(changingPwdDTO.getEmail());
        if (byEmail.isEmpty()) {
            throw new CustomValidationException("회원이 존재하지 않습니다.", CustomExceptionCode.NO_EXISTING_ID);
        }
        Member member = byEmail.get();
        member.setMemberPwd(bCryptPasswordEncoder.encode(changingPwdDTO.getPassword()));

    }
}
