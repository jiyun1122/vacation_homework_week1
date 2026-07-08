package mutsa_vacation_week1.demo.menu.dto;

import mutsa_vacation_week1.demo.menu.Menu;

public record MenuSummaryResponse(
        Long id,
        Long storeId,
        String name,
        Integer price
) {
    public static MenuSummaryResponse from(Menu menu) {
        return new MenuSummaryResponse(
                menu.getId(),
                menu.getStoreId(),
                menu.getName(),
                menu.getPrice()
        );
    }
}
