package mutsa_vacation_week1.demo.global.apiPayload.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MemberErrorCode implements BaseErrorCode {

    MEMBER_ALREADY_EXISTS(HttpStatus.CONFLICT, "MEMBER409_1", "이미 존재하는 loginId입니다."),
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "MEMBER401_1", "아이디 또는 비밀번호가 일치하지 않습니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER404_1", "존재하지 않는 회원입니다"),
    INSUFFICIENT_CREDIT(HttpStatus.BAD_REQUEST, "MEMBER400_1", "크레딧 잔액이 부족합니다."),
    SOCIAL_ACCOUNT_LOGIN_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "MEMBER400_2", "카카오로 가입된 계정입니다. 카카오 로그인을 이용해주세요.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

}
