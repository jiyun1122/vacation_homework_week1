package mutsa_vacation_week1.demo.global.apiPayload.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MenuErrorCode implements BaseErrorCode {

    MENU_NOT_FOUND(HttpStatus.NOT_FOUND, "MENU404_1", "메뉴를 찾을 수 없습니다."),
    MENU_OPTION_NOT_FOUND(HttpStatus.NOT_FOUND, "MENU404_2", "옵션을 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

}
