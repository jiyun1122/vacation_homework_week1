package mutsa_vacation_week1.demo.cart.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class CartItemUpdateRequest {
    private int quantity;
    private List<Long> menuOptionId;
}
