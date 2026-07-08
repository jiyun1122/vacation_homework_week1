package mutsa_vacation_week1.demo.global.exception;

// 에러 반환 포맷
public record ErrorResponse(
        String code,
        String message
) {
    public static ErrorResponse from(ErrorCode errorCode) {
        return new ErrorResponse(errorCode.getName(), errorCode.getMessage());
    }
}
