package mutsa_vacation_week1.demo.order.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

import java.util.List;

@Getter
public class OrderRequest {

    @NotEmpty(message = "장바구니 항목을 선택해주세요.")
    private List<Long> cartItemIds;

}
