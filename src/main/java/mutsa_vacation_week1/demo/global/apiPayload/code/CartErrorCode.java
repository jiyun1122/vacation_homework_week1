package mutsa_vacation_week1.demo.global.apiPayload.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CartErrorCode implements BaseErrorCode {


    CART_NOT_FOUND(HttpStatus.NOT_FOUND, "CART404_1", "장바구니가 없습니다."),
    MENU_NOT_FOUND(HttpStatus.NOT_FOUND, "CART404_2", "존재하지 않는 메뉴입니다."),
    MENU_OPTION_NOT_FOUND(HttpStatus.NOT_FOUND, "CART404_3", "존재하지 않는 옵션입니다."),
    CART_ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "CART404_4", "담긴 메뉴가 없습니다."), // 추가
    CART_ACCESS_DENIED(HttpStatus.FORBIDDEN, "CART403_1", "본인의 장바구니만 접근할 수 있습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

}
