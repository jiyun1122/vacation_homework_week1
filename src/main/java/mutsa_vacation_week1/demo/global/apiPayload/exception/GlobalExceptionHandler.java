package mutsa_vacation_week1.demo.global.apiPayload.exception;

import mutsa_vacation_week1.demo.cart.entity.Cart;
import mutsa_vacation_week1.demo.cart.repository.CartRepository;
import mutsa_vacation_week1.demo.global.apiPayload.ApiResponse;
import mutsa_vacation_week1.demo.global.apiPayload.code.BaseErrorCode;
import mutsa_vacation_week1.demo.global.apiPayload.code.CartErrorCode;
import mutsa_vacation_week1.demo.global.apiPayload.code.CommonErrorCode;
import mutsa_vacation_week1.demo.menu.entity.Menu;
import mutsa_vacation_week1.demo.menu.repository.MenuRepository;
import mutsa_vacation_week1.demo.menuOption.entity.MenuOption;
import mutsa_vacation_week1.demo.menuOption.repository.MenuOptionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<Void>> handleCustomException(CustomException e) {
        BaseErrorCode errorCode = e.getErrorCode();

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ApiResponse.onFailure(errorCode.getMessage()));
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        log.error("Unhandled exception", e);
        CommonErrorCode errorCode = CommonErrorCode.INTERNAL_SERVER_ERROR;

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ApiResponse.onFailure(errorCode.getMessage()));
    }

}