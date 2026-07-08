package mutsa_vacation_week1.demo.global.apiPayload.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum OrderErrorCode implements BaseErrorCode {

    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "ORDER404_1", "존재하지 않는 orderId입니다."),
    CART_ITEM_ID_NOT_FOUND(HttpStatus.NOT_FOUND, "ORDER404_2", "존재하지 않는 cartItemId입니다."),
    DUPLICATE_CART_ITEM_ID(HttpStatus.BAD_REQUEST, "ORDER400_1", "이미 사용된 cartItemId입니다."),
    ORDER_ALREADY_CANCELED(HttpStatus.BAD_REQUEST, "ORDER400_2", "이미 취소된 주문입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

}
