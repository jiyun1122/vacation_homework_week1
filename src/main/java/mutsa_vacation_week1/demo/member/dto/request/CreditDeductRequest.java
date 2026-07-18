package mutsa_vacation_week1.demo.member.dto.request;

import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreditDeductRequest {

    @Positive(message = "차감 금액은 0보다 커야 합니다.")
    private int amount;
}
