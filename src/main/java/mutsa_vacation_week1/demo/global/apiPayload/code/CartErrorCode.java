package mutsa_vacation_week1.demo.global.apiPayload.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CartErrorCode implements BaseErrorCode {

    CART_ACCESS_DENIED(HttpStatus.FORBIDDEN, "CART403_1", "본인의 장바구니만 접근할 수 있습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

}
