package mutsa_vacation_week1.demo.member.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreditDeductResponse {
    private Long memberId;
    private int amount;
    private int creditBefore;
    private int creditAfter;
}