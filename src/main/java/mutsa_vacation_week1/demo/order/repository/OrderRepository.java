package mutsa_vacation_week1.demo.order.repository;

import mutsa_vacation_week1.demo.member.entity.Member;
import mutsa_vacation_week1.demo.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByIdAndMember(Long id, Member member);
}

