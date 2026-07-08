package mutsa_vacation_week1.demo.cart.repository;

import mutsa_vacation_week1.demo.cart.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}