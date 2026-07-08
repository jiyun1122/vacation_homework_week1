package mutsa_vacation_week1.demo.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // Common
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "COMMON_001", "잘못된 입력값입니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "COMMON_002", "지원하지 않는 HTTP 메서드입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON_003", "서버 내부 오류가 발생했습니다."),

    // Member
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER_001", "존재하지 않는 회원입니다."),
    DUPLICATE_LOGIN_ID(HttpStatus.CONFLICT, "MEMBER_002", "이미 존재하는 아이디입니다."),
    INVALID_CHARGE_AMOUNT(HttpStatus.BAD_REQUEST, "MEMBER_003", "충전 금액은 0보다 커야 합니다."),
    NOT_ENOUGH_CREDIT(HttpStatus.BAD_REQUEST, "MEMBER_004", "크레딧이 부족합니다."),

    // Cart
    CART_NOT_FOUND(HttpStatus.NOT_FOUND, "CART_001", "장바구니가 없습니다."),
    CART_ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "CART_002", "담긴 메뉴가 없습니다."),
    INVALID_QUANTITY(HttpStatus.BAD_REQUEST, "CART_003", "수량은 1개 이상이어야 합니다."),

    // Menu (다른 팀원 담당이지만, 참조하는 쪽에서 발생 가능한 에러)
    MENU_NOT_FOUND(HttpStatus.NOT_FOUND, "MENU_001", "존재하지 않는 메뉴입니다."),
    MENU_OPTION_NOT_FOUND(HttpStatus.NOT_FOUND, "MENU_002", "존재하지 않는 메뉴 옵션입니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
