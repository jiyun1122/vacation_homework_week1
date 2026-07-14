package mutsa_vacation_week1.demo.global.apiPayload;


import lombok.Getter;

//응답 포맷을 클라이언트에게 전달하기 위한 부분으로, 성공과 실패의 응답 구조를 일치화시켜 프론트엔드와의 데이터 파싱을 쉽게
//이루어지도록 규칙을 정해둔 부분

@Getter
public class ApiResponse<T> {

    private final boolean success;
    private final String message;
    private final T result;

    public ApiResponse(boolean success, String message, T result) {
        this.success = success;
        this.message = message;
        this.result = result;
    }

    public static <T> ApiResponse<T> onSuccess(String message, T result) {
        return new ApiResponse<>(true, message, result);
    }

    public static <T> ApiResponse<T> onFailure(String message) {
        return new ApiResponse<>(false, message, null);
    }
}