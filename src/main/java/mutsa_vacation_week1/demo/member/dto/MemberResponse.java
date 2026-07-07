package mutsa_vacation_week1.demo.member.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

@Getter
@JsonPropertyOrder({"success", "message", "member"})
public class MemberResponse {

    private boolean success;
    private String message;
    private MemberInfo member;

    public MemberResponse(boolean success, String message, MemberInfo member) {
        this.success = success;
        this.message = message;
        this.member = member;
    }

}
