package mutsa_vacation_week1.demo.menuOption.dto.response;

import mutsa_vacation_week1.demo.menuOption.entity.MenuOption;

public record MenuOptionResponse(
        Long id,
        Long menuId,
        String option,
        String content,
        Integer price
) {
    public static MenuOptionResponse from(MenuOption menuOption) {
        return new MenuOptionResponse(
                menuOption.getId(),
                menuOption.getMenu().getId(),
                menuOption.getOption(),
                menuOption.getContent(),
                menuOption.getPrice()
        );
    }
}
