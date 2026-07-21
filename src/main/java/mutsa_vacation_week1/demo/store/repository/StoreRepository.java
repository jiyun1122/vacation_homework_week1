package mutsa_vacation_week1.demo.store.repository;


import mutsa_vacation_week1.demo.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {
    List<Store> findAllByCategory(String category);
}
