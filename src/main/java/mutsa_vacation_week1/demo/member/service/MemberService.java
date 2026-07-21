package mutsa_vacation_week1.demo.member.service;

import lombok.RequiredArgsConstructor;
import mutsa_vacation_week1.demo.cart.entity.Cart;
import mutsa_vacation_week1.demo.cart.repository.CartRepository;
import mutsa_vacation_week1.demo.member.dto.request.CreditDeductRequest;
import mutsa_vacation_week1.demo.member.dto.request.LoginRequest;
import mutsa_vacation_week1.demo.member.dto.request.SignupRequest;
import mutsa_vacation_week1.demo.member.dto.response.*;
import mutsa_vacation_week1.demo.member.entity.Member;
import mutsa_vacation_week1.demo.member.entity.Provider;
import mutsa_vacation_week1.demo.member.repository.MemberRepository;
import mutsa_vacation_week1.demo.global.security.jwt.JwtTokenProvider;
import mutsa_vacation_week1.demo.global.security.oauth2.KakaoOAuthClient;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import mutsa_vacation_week1.demo.global.apiPayload.exception.CustomException;
import mutsa_vacation_week1.demo.global.apiPayload.code.MemberErrorCode;

import java.util.Map;


@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final KakaoOAuthClient kakaoOAuthClient;

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

        if (member.getProvider() != Provider.LOCAL || member.getPassword() == null) {
            throw new CustomException(MemberErrorCode.SOCIAL_ACCOUNT_LOGIN_NOT_ALLOWED);
        }

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

    @Transactional
    public Member loginOrSignUpKakaoMember(String providerId, String nickname) {
        return memberRepository.findByProviderAndProviderId(Provider.KAKAO, providerId)
                .orElseGet(() -> {
                    Member saved = memberRepository.save(Member.ofKakao(providerId, nickname));
                    cartRepository.save(new Cart(saved.getId()));
                    return saved;
                });
    }

    /**
     * 프론트가 카카오로부터 직접 받은 인가코드를 넘겨주면, 백엔드가 카카오 서버와
     * 통신해서 토큰 교환 및 사용자 조회까지 대신 처리하고 자체 JWT를 발급한다.
     */
    @Transactional
    public LoginResponse kakaoLogin(String code) {
        String kakaoAccessToken = kakaoOAuthClient.getAccessToken(code);
        Map<String, Object> attributes = kakaoOAuthClient.getUserInfo(kakaoAccessToken);

        String providerId = String.valueOf(attributes.get("id"));
        @SuppressWarnings("unchecked")
        Map<String, Object> properties = (Map<String, Object>) attributes.getOrDefault("properties", Map.of());
        String nickname = (String) properties.getOrDefault("nickname", "카카오유저");

        Member member = loginOrSignUpKakaoMember(providerId, nickname);

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

    //credit 차감 로직

    @Transactional
    public CreditDeductResponse deductCredit(Long memberId, CreditDeductRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));

        int creditBefore = member.getCredit();

        member.deductCredit(request.getAmount()); // 내부에서 잔액부족 시 예외 던짐

        return CreditDeductResponse.builder()
                .memberId(member.getId())
                .amount(request.getAmount())
                .creditBefore(creditBefore)
                .creditAfter(member.getCredit())
                .build();
    }


}
