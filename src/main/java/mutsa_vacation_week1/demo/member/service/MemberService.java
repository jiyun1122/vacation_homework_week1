package mutsa_vacation_week1.demo.member.service;

import lombok.RequiredArgsConstructor;
import mutsa_vacation_week1.demo.cart.entity.Cart;
import mutsa_vacation_week1.demo.cart.repository.CartRepository;
import mutsa_vacation_week1.demo.member.dto.request.LoginRequest;
import mutsa_vacation_week1.demo.member.dto.request.SignupRequest;
import mutsa_vacation_week1.demo.member.dto.response.CreditChargeResponse;
import mutsa_vacation_week1.demo.member.dto.response.CreditResponse;
import mutsa_vacation_week1.demo.member.dto.response.LoginResponse;
import mutsa_vacation_week1.demo.member.dto.response.MemberInfo;
import mutsa_vacation_week1.demo.member.entity.Member;
import mutsa_vacation_week1.demo.member.repository.MemberRepository;
import mutsa_vacation_week1.demo.global.security.jwt.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import mutsa_vacation_week1.demo.global.apiPayload.exception.CustomException;
import mutsa_vacation_week1.demo.global.apiPayload.code.MemberErrorCode;


@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public MemberInfo signup(SignupRequest request) {

        if (memberRepository.findByLoginId(request.getLoginId()).isPresent()) {
            throw new CustomException(MemberErrorCode.MEMBER_ALREADY_EXISTS);
        }

        Member member = new Member(
                request.getLoginId(),
                passwordEncoder.encode(request.getPassword()),
                request.getName(),
                0
        );

        Member savedMember = memberRepository.save(member);

        Cart cart = new Cart(savedMember.getId());
        cartRepository.save(cart);

        return new MemberInfo(
            savedMember.getId(),
            savedMember.getLoginId(),
            savedMember.getName(),
            savedMember.getCredit()
        );

    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {

        Member member = memberRepository.findByLoginId(request.getLoginId())
                .orElseThrow(() -> new CustomException(MemberErrorCode.LOGIN_FAILED));

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new CustomException(MemberErrorCode.LOGIN_FAILED);
        }

        String accessToken = jwtTokenProvider.createAccessToken(member.getId(), member.getLoginId());

        MemberInfo memberInfo = new MemberInfo(
                member.getId(),
                member.getLoginId(),
                member.getName(),
                member.getCredit()
        );

        return new LoginResponse(accessToken, "Bearer", memberInfo);

    }

    @Transactional(readOnly = true)
    public MemberInfo getMyInfo(Long memberId) {
        Member member = findMemberById(memberId);

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
                .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));
    }

}
