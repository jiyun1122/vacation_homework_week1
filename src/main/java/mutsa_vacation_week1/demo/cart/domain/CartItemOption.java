package mutsa_vacation_week1.demo.cart.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class CartItemOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_item_id")
    private CartItem cartItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_option_id")
    private MenuOption menuOption;

    public CartItemOption(MenuOption menuOption) {
        this.menuOption = menuOption;
    }

    public void setCartItem(CartItem cartItem) {
        this.cartItem = cartItem;
    }
}
