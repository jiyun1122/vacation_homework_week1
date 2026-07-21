package mutsa_vacation_week1.demo.cart.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CartItemUpdateRequest {
    private int quantity;
    private List<Long> menuOptionId;
}
