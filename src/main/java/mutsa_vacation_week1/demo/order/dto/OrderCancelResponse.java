package mutsa_vacation_week1.demo.order.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

@Getter
@JsonPropertyOrder({"orderId", "status", "refundedCredit"})
public class OrderCancelResponse {

    private Long orderId;
    private String status;
    private int refundedCredit;

    public OrderCancelResponse(Long orderId, String status, int refundedCredit) {
        this.orderId = orderId;
        this.status = status;
        this.refundedCredit = refundedCredit;
    }

}
