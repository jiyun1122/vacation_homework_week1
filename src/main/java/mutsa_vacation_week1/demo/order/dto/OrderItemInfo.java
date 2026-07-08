package mutsa_vacation_week1.demo.order.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

import java.util.List;

@Getter
@JsonPropertyOrder({"orderItemId", "menuId", "menuName", "quantity", "orderPrice", "options"})
public class OrderItemInfo {

    private Long orderItemId;
    private Long menuId;
    private String menuName;
    private int quantity;
    private int orderPrice;
    private List<OrderOptionInfo> options;

    public OrderItemInfo(Long orderItemId, Long menuId, String menuName,
                         int quantity, int orderPrice, List<OrderOptionInfo> options) {
        this.orderItemId = orderItemId;
        this.menuId = menuId;
        this.menuName = menuName;
        this.quantity = quantity;
        this.orderPrice = orderPrice;
        this.options = options;
    }

}
