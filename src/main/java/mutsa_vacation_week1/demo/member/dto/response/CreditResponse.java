package mutsa_vacation_week1.demo.member.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreditResponse {
    private Long memberId;
    private int credit;
}
