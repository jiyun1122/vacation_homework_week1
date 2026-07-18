package mutsa_vacation_week1.demo.global.security.oauth2;

import lombok.Getter;
import mutsa_vacation_week1.demo.member.entity.Member;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Getter
public class CustomOAuth2User implements OAuth2User {

    private final Member member;
    private final Map<String, Object> attributes;

    public CustomOAuth2User(Member member, Map<String, Object> attributes) {
        this.member = member;
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends org.springframework.security.core.GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getName() {
        return member.getProviderId();
    }
}
