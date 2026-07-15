package mutsa_vacation_week1.demo.store.repository;


import mutsa_vacation_week1.demo.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {
}
