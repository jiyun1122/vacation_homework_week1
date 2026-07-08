package mutsa_vacation_week1.demo.menu.dto.response;

import mutsa_vacation_week1.demo.menu.entity.Menu;
import mutsa_vacation_week1.demo.menuOption.dto.response.MenuOptionResponse;

import java.util.List;

public record MenuDetailResponse(
        Long id,
        Long storeId,
        String name,
        String description,
        Integer price,
        List<MenuOptionResponse> menuOptions
) {
    public static MenuDetailResponse from(Menu menu, List<MenuOptionResponse> menuOptions) {
        return new MenuDetailResponse(
                menu.getId(),
                menu.getStoreId(),
                menu.getName(),
                menu.getDescription(),
                menu.getPrice(),
                menuOptions
        );
    }
}
