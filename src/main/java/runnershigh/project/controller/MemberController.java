package runnershigh.project.controller;

import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import runnershigh.project.domain.Agree;
import runnershigh.project.domain.Member;
import runnershigh.project.domain.MemberRole;
import runnershigh.project.dto.*;
import runnershigh.project.handler.CommonResDTO;
import runnershigh.project.handler.CustomExceptionCode;
import runnershigh.project.handler.CustomValidationException;
import runnershigh.project.security.util.JwtTokenizer;
import runnershigh.project.service.MemberService;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final RedisTemplate redisTemplate;
    private final DefaultMessageService defaultMessageService;
    private final MemberService memberService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenizer jwtTokenizer;


    private final static Duration contactDuration = Duration.ofMinutes(3);
    private final static Duration refreshDuration = Duration.ofDays(7);

    @PostMapping("/api/v1/auth/sms")
    public ResponseEntity sendingSms(@RequestBody @Valid PhoneNumberDTO phoneNumberDTO, BindingResult result) {
        if (result.hasErrors()) {
            throw new CustomValidationException("빈 값이 존재합니다.", CustomExceptionCode.NULL_FAIL);
        }

        Random random = new Random();
        String numStr = "";

        for (int i = 0; i < 6; i++) {
            String ran = Integer.toString(random.nextInt(10));
            numStr += ran;
        }

        String phoneNumber = phoneNumberDTO.getPhoneNumber();
        Message message = new Message();
        message.setFrom("01065512471");
        message.setTo(phoneNumber);
        message.setText("다음 인증번호를 입력해주세요 " + numStr);


        SingleMessageSentResponse response = defaultMessageService.sendOne(new SingleMessageSendingRequest(message));
        System.out.println(response);
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set(phoneNumber, numStr, contactDuration);

        return ResponseEntity.status(HttpStatus.OK).body(new CommonResDTO("1", "인증번호 발급 성공", null));
    }

    @PostMapping("/api/v1/auth/smsCheck")
    public ResponseEntity smsCheck(@RequestBody @Valid SmsCheckDTO smsCheckDTO, BindingResult result) {
        if (result.hasErrors()) {
            throw new CustomValidationException("빈 값이 존재합니다.", CustomExceptionCode.NULL_FAIL);
        }

        ValueOperations valueOperations = redisTemplate.opsForValue();
        String savedAuthNumber = (String) valueOperations.get(smsCheckDTO.getPhoneNumber());

        if (smsCheckDTO.getAuthNumber().equals(savedAuthNumber)) {
            return ResponseEntity.status(HttpStatus.OK).body(new CommonResDTO<>("1", "인증번호 일치", null));
        } else {
            throw new CustomValidationException("인증번호 불일치", CustomExceptionCode.AUTH_NUMBER_FAIL);
        }

    }

    @PostMapping("/api/v1/enquiry/idCheck")
    public ResponseEntity idCheck(@RequestBody @Valid MemberIdCheckDTO memberIdCheckDTO, BindingResult result) {
        if (result.hasErrors()) {
            throw new CustomValidationException("빈 값이 존재합니다.", CustomExceptionCode.NULL_FAIL);
        }
        if (memberService.checkEmail(memberIdCheckDTO.getEmail())) {
            throw new CustomValidationException("이미 존재하는 Email입니다.", CustomExceptionCode.EXISTING_ID);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(new CommonResDTO<>("1", "사용할 수 있는 Email입니다.", null));
        }

    }

    @PostMapping("/api/v1/join")
    public ResponseEntity join(@RequestBody @Valid MemberJoinDTO memberJoinDTO, BindingResult result) {
        if (result.hasErrors()) {
            throw new CustomValidationException("빈 값이 존재합니다.", CustomExceptionCode.NULL_FAIL);
        }

        Member member = new Member();
        AgreeDTO agreeDTO = memberJoinDTO.getAgreeDTO();
        Agree agree = new Agree(1,1, agreeDTO.getFirstSelAgree(), agreeDTO.getSecondSelAgree(), agreeDTO.getThirdSelAgree());
        member.setEmail(memberJoinDTO.getMemberEmail());
        member.setName(memberJoinDTO.getMemberNm());
        member.setMemberPwd(bCryptPasswordEncoder.encode(memberJoinDTO.getMemberPwd()));
        member.setMemberPhone(memberJoinDTO.getMemberPhone());
        member.setBirth(memberJoinDTO.getBirth());
        member.setAgree(agree);

        Member savedMember = memberService.join(member);

        List<MemberRole> memberRoles = savedMember.getMemberRoles();
        String role = "";
        for (MemberRole memberRole : memberRoles) {
            role += memberRole.getRole().getRole() + " ";
        }

        MemberJoinResDTO memberJoinResDTO = new MemberJoinResDTO();
        memberJoinResDTO.setId(savedMember.getId());
        memberJoinResDTO.setEmail(savedMember.getEmail());
        memberJoinResDTO.setMemberPwd(savedMember.getMemberPwd());
        memberJoinResDTO.setMemberPhone(savedMember.getMemberPhone());
        memberJoinResDTO.setRole(role);

        return ResponseEntity.status(HttpStatus.OK).body(new CommonResDTO<>("1", "회원가입 성공", memberJoinResDTO));
    }


    @PostMapping("/api/v1/login")
    public ResponseEntity login(@RequestBody @Valid MemberLoginDTO memberLoginDTO, BindingResult result) {
        if (result.hasErrors()) {
            throw new CustomValidationException("빈 값이 존재합니다.",CustomExceptionCode.NULL_FAIL);
        }
        Optional<Member> findMember = memberService.findByEmail(memberLoginDTO.getMemberEmail());
        if (findMember.isEmpty()) {
            throw new CustomValidationException("로그인 실패",CustomExceptionCode.LOGIN_FAIL);
        }
        Member member = findMember.get();

        if (!bCryptPasswordEncoder.matches(memberLoginDTO.getMemberPwd(), member.getMemberPwd())) {
            throw new CustomValidationException("로그인 실패",CustomExceptionCode.LOGIN_FAIL);
        }

        List<String> roles = member.getMemberRoles().stream().map(MemberRole::getRoleName).collect(Collectors.toList());

        String accessToken = jwtTokenizer.createAccessToken(member.getId(), member.getEmail(), member.getName(), roles);
        String refreshToken = jwtTokenizer.createRefreshToken(member.getId(), member.getEmail(), member.getName(), roles);

        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(member.getId() + member.getEmail(), refreshToken, refreshDuration);

        MemberLoginResponseDTO memberLoginResponseDTO = new MemberLoginResponseDTO();
        memberLoginResponseDTO.setAccessToken(accessToken);
        memberLoginResponseDTO.setRefreshToken(refreshToken);
        memberLoginResponseDTO.setMemberEmail(member.getEmail());
        memberLoginResponseDTO.setId(member.getId());
        memberLoginResponseDTO.setName(member.getName());

        CommonResDTO commonResDTO = new CommonResDTO<>("1", "로그인 성공", memberLoginResponseDTO);

        return ResponseEntity.status(HttpStatus.OK).body(commonResDTO);

    }

    @PostMapping("/api/v1/logout")
    public ResponseEntity logout(@RequestBody @Valid MemberLogoutDTO memberLogoutDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new CustomValidationException("빈 값이 존재합니다.",CustomExceptionCode.NULL_FAIL);
        }

        ValueOperations valueOperations = redisTemplate.opsForValue();
        String savedToken = (String) valueOperations.get(memberLogoutDTO.getMemberId() + memberLogoutDTO.getMemberEmail());
        if (savedToken.equals(memberLogoutDTO.getRefreshToken())) {
            redisTemplate.delete(memberLogoutDTO.getMemberId() + memberLogoutDTO.getMemberEmail());
            return ResponseEntity.status(HttpStatus.OK).body(new CommonResDTO<>("1", "로그아웃 성공", null));
        } else {
            throw new CustomValidationException("로그아웃 실패", CustomExceptionCode.REFRESH_FAIL);
        }

    }

    @PostMapping("/api/v1/refresh")
    public ResponseEntity refresh(@RequestBody RefreshTokenDTO refreshTokenDTO) {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String savedToken = (String) valueOperations.get(refreshTokenDTO.getMemberId() + refreshTokenDTO.getMemberEmail());
        if (savedToken.equals(refreshTokenDTO.getRefreshToken())) {

            Claims claims = jwtTokenizer.parseRefreshToken(refreshTokenDTO.getRefreshToken());
            Long userId = Long.valueOf((Integer) claims.get("userId"));
            List roles = (List) claims.get("roles");
            String email = claims.getSubject();
            String name = (String)claims.get("name");

            String accessToken = jwtTokenizer.createAccessToken(userId, email, name, roles);

            MemberLoginResponseDTO memberLoginResponseDTO = new MemberLoginResponseDTO();
            memberLoginResponseDTO.setAccessToken(accessToken);
            memberLoginResponseDTO.setId(userId);
            memberLoginResponseDTO.setMemberEmail(email);
            memberLoginResponseDTO.setName(name);
            memberLoginResponseDTO.setRefreshToken(refreshTokenDTO.getRefreshToken());

            CommonResDTO<MemberLoginResponseDTO> commonResDTO = new CommonResDTO<>("1", "refreshToken 재발급 성공", memberLoginResponseDTO);
            return ResponseEntity.status(HttpStatus.OK).body(commonResDTO);

        } else {
            throw new CustomValidationException("토큰 재발급 실패", CustomExceptionCode.REFRESH_FAIL);
        }


    }

}
