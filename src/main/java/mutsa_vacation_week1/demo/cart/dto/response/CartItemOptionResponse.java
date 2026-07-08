package mutsa_vacation_week1.demo.cart.dto.response;


import lombok.Builder;
import lombok.Getter;
import mutsa_vacation_week1.demo.cart.domain.CartItemOption;

@Getter
@Builder
public class CartItemOptionResponse {
    private Long cartItemOptionId;
    private Long menuOptionId;
    private String option;
    private String content;
    private int price;

    public static CartItemOptionResponse from(CartItemOption option) {
        return CartItemOptionResponse.builder()
                .cartItemOptionId(option.getId())
                .menuOptionId(option.getMenuOption().getId())
                .option(option.getMenuOption().getOption())
                .content(option.getMenuOption().getContent())
                .price(option.getMenuOption().getPrice())
                .build();
    }
}
