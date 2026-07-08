package mutsa_vacation_week1.demo.global.apiPayload.exception;

import lombok.Getter;
import mutsa_vacation_week1.demo.global.apiPayload.code.BaseErrorCode;

@Getter
public class CustomException extends RuntimeException{

    private final BaseErrorCode errorCode;

    public CustomException(BaseErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

}
