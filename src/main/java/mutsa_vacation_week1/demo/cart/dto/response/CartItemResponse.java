package mutsa_vacation_week1.demo.cart.dto.response;

import lombok.Builder;
import lombok.Getter;
import mutsa_vacation_week1.demo.cart.entity.CartItem;

import java.util.List;

@Getter
@Builder
public class CartItemResponse {
    private Long cartItemId;
    private Long menuId;
    private String menuName;
    private int quantity;
    private List<CartItemOptionResponse> options;

    public static CartItemResponse from(CartItem cartItem) {
        return CartItemResponse.builder()
                .cartItemId(cartItem.getId())
                .menuId(cartItem.getMenu().getId())
                .menuName(cartItem.getMenu().getName())
                .quantity(cartItem.getQuantity())
                .options(cartItem.getOptions().stream()
                        .map(CartItemOptionResponse::from)
                        .toList())
                .build();
    }
}