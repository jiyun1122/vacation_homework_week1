package mutsa_vacation_week1.demo.global.security.oauth2;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import mutsa_vacation_week1.demo.member.entity.Member;
import mutsa_vacation_week1.demo.global.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;

    @Value("${app.oauth2.frontend-redirect-uri}")
    private String frontendRedirectUri;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                         Authentication authentication) throws IOException {
        CustomOAuth2User principal = (CustomOAuth2User) authentication.getPrincipal();
        Member member = principal.getMember();

        String accessToken = jwtTokenProvider.createAccessToken(member.getId(), member.getLoginId());

        String redirectUrl = UriComponentsBuilder.fromUriString(frontendRedirectUri)
                .queryParam("token", accessToken)
                .build()
                .toUriString();

        response.sendRedirect(redirectUrl);
    }
}
