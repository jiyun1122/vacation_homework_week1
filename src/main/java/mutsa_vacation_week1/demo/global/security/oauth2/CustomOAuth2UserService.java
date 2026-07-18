package mutsa_vacation_week1.demo.global.security.oauth2;

import lombok.RequiredArgsConstructor;
import mutsa_vacation_week1.demo.member.entity.Member;
import mutsa_vacation_week1.demo.member.service.MemberService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberService memberService;
    private final OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        Map<String, Object> attributes = oAuth2User.getAttributes();
        @SuppressWarnings("unchecked")
        Map<String, Object> properties = (Map<String, Object>) attributes.getOrDefault("properties", Map.of());

        String providerId = String.valueOf(attributes.get("id"));
        String nickname = (String) properties.getOrDefault("nickname", "카카오유저");

        Member member = memberService.loginOrSignUpKakaoMember(providerId, nickname);

        return new CustomOAuth2User(member, attributes);
    }
}
