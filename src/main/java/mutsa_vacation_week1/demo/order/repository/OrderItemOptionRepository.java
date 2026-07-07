package mutsa_vacation_week1.demo.order.repository;

import mutsa_vacation_week1.demo.order.entity.OrderItem;
import mutsa_vacation_week1.demo.order.entity.OrderItemOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemOptionRepository extends JpaRepository<OrderItemOption, Long> {

    List<OrderItemOption> findByOrderItem(OrderItem orderItem);

}
