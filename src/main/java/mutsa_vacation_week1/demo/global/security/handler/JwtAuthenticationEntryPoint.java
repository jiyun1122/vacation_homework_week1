package mutsa_vacation_week1.demo.global.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import mutsa_vacation_week1.demo.global.apiPayload.ApiResponse;
import mutsa_vacation_week1.demo.global.apiPayload.code.AuthErrorCode;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        String reason = (String) request.getAttribute("authErrorCode");
        AuthErrorCode errorCode = switch (reason == null ? "" : reason) {
            case "EXPIRED_TOKEN" -> AuthErrorCode.EXPIRED_TOKEN;
            case "INVALID_TOKEN" -> AuthErrorCode.INVALID_TOKEN;
            default -> AuthErrorCode.UNAUTHORIZED;
        };

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(errorCode.getHttpStatus().value());

        ApiResponse<Void> body = ApiResponse.onFailure(errorCode.getMessage());

        response.getWriter().write(objectMapper.writeValueAsString(body));
    }
}