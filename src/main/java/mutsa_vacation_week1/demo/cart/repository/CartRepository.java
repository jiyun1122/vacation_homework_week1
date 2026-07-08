package mutsa_vacation_week1.demo.cart.repository;

import mutsa_vacation_week1.demo.cart.entity.Cart;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    @EntityGraph(attributePaths = {"cartItems", "cartItems.menu", "cartItems.options", "cartItems.options.menuOption"})
    Optional<Cart> findByMemberId(Long memberId);
}