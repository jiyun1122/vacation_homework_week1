package mutsa_vacation_week1.demo.global.apiPayload;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

@Getter
@JsonPropertyOrder({"success", "message", "result"})
public class ApiResponse<T> {

    private final boolean success;
    private final String message;
    private final T result;

    private ApiResponse(boolean success, String message, T result) {
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
