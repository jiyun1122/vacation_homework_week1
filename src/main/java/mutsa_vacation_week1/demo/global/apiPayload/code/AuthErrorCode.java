package mutsa_vacation_week1.demo.global.apiPayload.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

//baseErrorcode를 상속해서 http,비지니스 에러코드, 에러 메시지를 표시하는 표준화된 에러 코드들 모음집

@Getter
@AllArgsConstructor
public enum AuthErrorCode implements BaseErrorCode {

    MISSING_AUTH_HEADER(HttpStatus.UNAUTHORIZED, "AUTH401_1", "인증 토큰이 필요합니다."),
    INVALID_TOKEN_FORMAT(HttpStatus.UNAUTHORIZED, "AUTH401_2", "토큰 형식이 올바르지 않습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH401_3", "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH401_4", "만료된 토큰입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "AUTH401_5", "인증이 필요합니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "AUTH403_1", "접근 권한이 없습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

}
