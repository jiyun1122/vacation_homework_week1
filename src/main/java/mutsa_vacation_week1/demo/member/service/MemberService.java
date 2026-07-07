package mutsa_vacation_week1.demo.member.service;

import lombok.RequiredArgsConstructor;
import mutsa_vacation_week1.demo.member.domain.Member;
import mutsa_vacation_week1.demo.member.dto.response.CreditChargeResponse;
import mutsa_vacation_week1.demo.member.dto.response.CreditResponse;
import mutsa_vacation_week1.demo.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

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