package mutsa_vacation_week1.demo.menu;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mutsa_vacation_week1.demo.menuOption.MenuOption;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long storeId;

    private String storeName;

    private String name;

    private String description;

    private Integer price;

    @OneToMany(mappedBy = "menu")
    private List<MenuOption> menuOptions = new ArrayList<>();

    @Builder
    public Menu(Long storeId, String storeName, String name, String description, Integer price) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.name = name;
        this.description = description;
        this.price = price;
    }
}
