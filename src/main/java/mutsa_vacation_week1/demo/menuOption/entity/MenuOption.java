package mutsa_vacation_week1.demo.menuOption.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mutsa_vacation_week1.demo.menu.entity.Menu;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MenuOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @Column(name = "option_value")
    private String option;

    private String content;

    private Integer price;

    @Builder
    public MenuOption(Menu menu, String option, String content, Integer price) {
        this.menu = menu;
        this.option = option;
        this.content = content;
        this.price = price;
    }

    public void update(String content, Integer price) {
        this.content = content;
        this.price = price;
    }
}
