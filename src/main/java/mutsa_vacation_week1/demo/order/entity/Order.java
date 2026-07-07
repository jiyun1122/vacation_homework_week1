package mutsa_vacation_week1.demo.order.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mutsa_vacation_week1.demo.member.entity.Member;

@Entity
@Getter
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "member_id")
    private Member member;

    @Column(nullable = false)
    private int totalPrice;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    public Order(Member member, int totalPrice) {
        this.member = member;
        this.totalPrice = totalPrice;
        this.status = OrderStatus.ORDER_COMPLETED;
    }

    public void cancel() {
        this.status = OrderStatus.CANCELLED;
    }

}
