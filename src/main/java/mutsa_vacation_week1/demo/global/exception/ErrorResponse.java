package mutsa_vacation_week1.demo.global.exception;

public record ErrorResponse(
        String code,
        String message
) {
    public static ErrorResponse from(ErrorCode errorCode) {
        return new ErrorResponse(errorCode.name(), errorCode.getMessage());
    }
}
