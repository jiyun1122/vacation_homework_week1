package mutsa_vacation_week1.demo.order.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

import java.util.List;

@Getter
@JsonPropertyOrder({"orderId", "totalPrice", "status", "stores"})
public class OrderResponse {

    private Long orderId;
    private int totalPrice;
    private String status;
    private List<OrderStoreGroup> stores;

    public OrderResponse(Long orderId, int totalPrice, String status, List<OrderStoreGroup> stores) {
        this.orderId = orderId;
        this.totalPrice = totalPrice;
        this.status = status;
        this.stores = stores;
    }

}
