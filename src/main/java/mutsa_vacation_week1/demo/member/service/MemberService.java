package mutsa_vacation_week1.demo.member.service;

import lombok.RequiredArgsConstructor;
import mutsa_vacation_week1.demo.global.apiPayload.exception.CustomException;
import mutsa_vacation_week1.demo.global.apiPayload.exception.MemberErrorCode;
import mutsa_vacation_week1.demo.member.dto.*;
import mutsa_vacation_week1.demo.member.entity.Member;
import mutsa_vacation_week1.demo.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;

    @Transactional
    public MemberInfo signup(SignupRequest request) {

        if (memberRepository.findByLoginId(request.getLoginId()).isPresent()) {
            throw new CustomException(MemberErrorCode.MEMBER_ALREADY_EXISTS);
        }

        Member member = new Member(
                request.getLoginId(),
                request.getPassword(),
                request.getName(),
                0
        );

        Member savedMember = memberRepository.save(member);

        Cart cart = new Cart(savedMember);
        cartRepository.save(cart);

        return new MemberInfo(
            savedMember.getId(),
            savedMember.getLoginId(),
            savedMember.getName(),
            savedMember.getCredit()
        );

    }

    @Transactional(readOnly = true)
    public MemberInfo login(LoginRequest request) {

        Member member = memberRepository.findByLoginId(request.getLoginId())
                .orElseThrow(() -> new CustomException(MemberErrorCode.LOGIN_FAILED));

        if (!member.getPassword().equals(request.getPassword())) {
            throw new CustomException(MemberErrorCode.LOGIN_FAILED);
        }

        return new MemberInfo(
                member.getId(),
                member.getLoginId(),
                member.getName(),
                member.getCredit()
        );

    }

    public void logout() {}

}
