package mutsa_vacation_week1.demo.menu.repository;

import mutsa_vacation_week1.demo.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {
}
