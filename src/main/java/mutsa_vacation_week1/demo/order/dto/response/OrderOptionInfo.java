package mutsa_vacation_week1.demo.order.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

@Getter
@JsonPropertyOrder({"orderId", "content", "orderPrice"})
public class OrderOptionInfo {

    private Long orderId;
    private String content;
    private int orderPrice;

    public OrderOptionInfo(Long orderId, String content, int orderPrice) {
        this.orderId = orderId;
        this.content = content;
        this.orderPrice = orderPrice;
    }

}
