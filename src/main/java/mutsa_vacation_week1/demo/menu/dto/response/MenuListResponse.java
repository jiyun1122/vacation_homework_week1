package mutsa_vacation_week1.demo.menu.dto.response;

import java.util.List;

public record MenuListResponse(
        Long storeId,
        String storeName,
        List<MenuSummaryResponse> menus
) {
}
