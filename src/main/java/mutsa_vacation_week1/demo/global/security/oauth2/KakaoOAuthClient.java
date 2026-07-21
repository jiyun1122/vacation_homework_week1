package mutsa_vacation_week1.demo.global.security.oauth2;

import mutsa_vacation_week1.demo.global.apiPayload.code.AuthErrorCode;
import mutsa_vacation_week1.demo.global.apiPayload.exception.CustomException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.Map;

/**
 * 프론트가 카카오로부터 직접 받은 인가코드를 백엔드에 넘겨주면,
 * 백엔드가 카카오 서버와 직접 통신해서 토큰 교환/사용자 정보 조회를 대신 처리한다.
 * Spring Security의 자동 OAuth2 로그인 필터(/login/oauth2/code/kakao)와는 별개의 경로다.
 */
@Component
public class KakaoOAuthClient {

    private final RestClient restClient = RestClient.create();

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String clientSecret;

    @Value("${app.oauth2.kakao-redirect-uri}")
    private String redirectUri;

    public String getAccessToken(String code) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        if (clientSecret != null && !clientSecret.isBlank()) {
            body.add("client_secret", clientSecret);
        }
        body.add("redirect_uri", redirectUri);
        body.add("code", code);

        try {
            KakaoTokenResponse response = restClient.post()
                    .uri("https://kauth.kakao.com/oauth/token")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(body)
                    .retrieve()
                    .body(KakaoTokenResponse.class);

            if (response == null || response.accessToken() == null) {
                throw new CustomException(AuthErrorCode.KAKAO_TOKEN_REQUEST_FAILED);
            }
            return response.accessToken();
        } catch (RestClientException e) {
            throw new CustomException(AuthErrorCode.KAKAO_TOKEN_REQUEST_FAILED);
        }
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getUserInfo(String kakaoAccessToken) {
        try {
            Map<String, Object> attributes = restClient.get()
                    .uri("https://kapi.kakao.com/v2/user/me")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + kakaoAccessToken)
                    .retrieve()
                    .body(Map.class);

            if (attributes == null) {
                throw new CustomException(AuthErrorCode.KAKAO_USERINFO_REQUEST_FAILED);
            }
            return attributes;
        } catch (RestClientException e) {
            throw new CustomException(AuthErrorCode.KAKAO_USERINFO_REQUEST_FAILED);
        }
    }
}
