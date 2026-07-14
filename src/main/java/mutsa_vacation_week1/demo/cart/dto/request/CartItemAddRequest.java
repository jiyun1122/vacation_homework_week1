package mutsa_vacation_week1.demo.cart.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class CartItemAddRequest {
    private Long menuId;
    private int quantity;
    private List<Long> menuOptionIds;
}