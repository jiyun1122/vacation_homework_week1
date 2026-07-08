package mutsa_vacation_week1.demo.member.service;

import lombok.RequiredArgsConstructor;
import mutsa_vacation_week1.demo.member.dto.*;
import mutsa_vacation_week1.demo.member.entity.Member;
import mutsa_vacation_week1.demo.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import mutsa_vacation_week1.demo.global.exception.CustomException;
import mutsa_vacation_week1.demo.global.exception.ErrorCode;


@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;

    @Transactional
    public MemberInfo signup(SignupRequest request) {

        if (memberRepository.findByLoginId(request.getLoginId()).isPresent()) {
            throw new CustomException(ErrorCode.MEMBER_ALREADY_EXISTS);
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
                .orElseThrow(() -> new CustomException(ErrorCode.LOGIN_FAILED));

        if (!member.getPassword().equals(request.getPassword())) {
            throw new CustomException(ErrorCode.LOGIN_FAILED);
        }

        return new MemberInfo(
                member.getId(),
                member.getLoginId(),
                member.getName(),
                member.getCredit()
        );

    }

    public void logout() {}
  
   // 크레딧 충전
    @Transactional
    public CreditChargeResponse chargeCredit(Long memberId, int amount) {
        Member member = findMemberById(memberId);

        int before = member.getCredit();
        member.chargeCredit(amount);
        int after = member.getCredit();

        return CreditChargeResponse.builder()
                .memberId(member.getId())
                .amount(amount)
                .creditBefore(before)
                .creditAfter(after)
                .build();
    }

    // 크레딧 조회
    public CreditResponse getCredit(Long memberId) {
        Member member = findMemberById(memberId);

        return CreditResponse.builder()
                .memberId(member.getId())
                .credit(member.getCredit())
                .build();
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
    }

}
