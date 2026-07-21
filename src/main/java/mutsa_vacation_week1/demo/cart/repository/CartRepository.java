package mutsa_vacation_week1.demo.cart.repository;

import mutsa_vacation_week1.demo.cart.entity.Cart;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    // cartItems와 cartItems.options를 동시에 fetch join하면 Hibernate가
    // MultipleBagFetchException을 던지므로(둘 다 List 컬렉션), options는 지연 로딩으로 남겨둔다.
    // 대신 이 메서드를 호출하는 서비스 메서드는 @Transactional(readOnly = true)로
    // 감싸서, 응답 DTO로 변환하는 시점까지 영속성 컨텍스트가 열려있도록 해야 한다.
    @EntityGraph(attributePaths = {"cartItems", "cartItems.menu"})
    Optional<Cart> findByMemberId(Long memberId);
}