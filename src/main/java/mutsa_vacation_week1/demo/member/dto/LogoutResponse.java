package mutsa_vacation_week1.demo.member.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

@Getter
@JsonPropertyOrder({"success", "message"})
public class LogoutResponse {

    private boolean success;
    private String message;

    public LogoutResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

}
