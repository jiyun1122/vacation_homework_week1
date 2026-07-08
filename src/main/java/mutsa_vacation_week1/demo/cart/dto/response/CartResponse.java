package mutsa_vacation_week1.demo.cart.dto.response;


import lombok.Builder;
import lombok.Getter;
import mutsa_vacation_week1.demo.cart.entity.Cart;

import java.util.List;

@Getter
@Builder
public class CartResponse {
    private Long cartId;
    private List<CartItemResponse> items;

    public static CartResponse from(Cart cart) {
        return CartResponse.builder()
                .cartId(cart.getId())
                .items(cart.getCartItems().stream()
                        .map(CartItemResponse::from)
                        .toList())
                .build();
    }
}
