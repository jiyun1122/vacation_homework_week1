package mutsa_vacation_week1.demo.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class KakaoLoginRequest {

    @NotBlank(message = "인가코드는 필수입니다.")
    private String code;

}
