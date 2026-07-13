package mutsa_vacation_week1.demo.member.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

@Getter
@JsonPropertyOrder({"accessToken", "tokenType", "member"})
public class LoginResponse {

    private final String accessToken;
    private final String tokenType;
    private final MemberInfo member;

    public LoginResponse(String accessToken, String tokenType, MemberInfo member) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.member = member;
    }

}
