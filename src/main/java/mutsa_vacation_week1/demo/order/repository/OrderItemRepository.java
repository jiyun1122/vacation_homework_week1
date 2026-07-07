package mutsa_vacation_week1.demo.order.repository;

import mutsa_vacation_week1.demo.order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
