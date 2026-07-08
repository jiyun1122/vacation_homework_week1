package mutsa_vacation_week1.demo.order.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

import java.util.List;

@Getter
@JsonPropertyOrder({"storeId", "storeName", "items"})
public class OrderStoreGroup {

    private Long storeId;
    private String storeName;
    private List<OrderItemInfo> items;

    public OrderStoreGroup(Long storeId, String storeName, List<OrderItemInfo> items) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.items = items;
    }

}
