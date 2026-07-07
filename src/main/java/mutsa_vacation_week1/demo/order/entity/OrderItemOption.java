package mutsa_vacation_week1.demo.order.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "order_item_option")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItemOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "order_item_id")
    private OrderItem orderItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "menu_option_id")
    private MenuOption menuOption;

    @Column(nullable = false)
    private int orderPrice;

    public OrderItemOption(OrderItem orderItem, MenuOption menuOption, int orderPrice) {
        this.orderItem = orderItem;
        this.menuOption = menuOption;
        this.orderPrice = orderPrice;
    }

}
