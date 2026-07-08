package mutsa_vacation_week1.demo.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
// 에러 정의서 
public enum ErrorCode {
    // MENU 도메인
    MENU_NOT_FOUND(HttpStatus.NOT_FOUND, "MENU404_1", "메뉴를 찾을 수 없습니다."),
    MENU_OPTION_NOT_FOUND(HttpStatus.NOT_FOUND, "MENU404_2", "옵션을 찾을 수 없습니다."),

    // COMMON 도메인
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500_1", "서버 내부 오류가 발생했습니다.");

    private final HttpStatus httpStatus;
    private final String name;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String name, String message) {
        this.httpStatus = httpStatus;
        this.name = name;
        this.message = message;
    }
}
