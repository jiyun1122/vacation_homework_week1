package mutsa_vacation_week1.demo.menu;

import lombok.RequiredArgsConstructor;
import mutsa_vacation_week1.demo.global.exception.CustomException;
import mutsa_vacation_week1.demo.global.exception.ErrorCode;
import mutsa_vacation_week1.demo.menu.dto.MenuDetailResponse;
import mutsa_vacation_week1.demo.menu.dto.MenuListResponse;
import mutsa_vacation_week1.demo.menu.dto.MenuSummaryResponse;
import mutsa_vacation_week1.demo.menuOption.dto.MenuOptionResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuService {

    private final MenuRepository menuRepository;

    public MenuListResponse getMenuList() {
        List<Menu> menus = menuRepository.findAll();

        Long storeId = null;
        String storeName = null;
        if (!menus.isEmpty()) {
            Menu firstMenu = menus.get(0);
            storeId = firstMenu.getStoreId();
            storeName = firstMenu.getStoreName();
        }

        List<MenuSummaryResponse> menuSummaries = menus.stream()
                .map(MenuSummaryResponse::from)
                .toList();

        return new MenuListResponse(storeId, storeName, menuSummaries);
    }

    public MenuDetailResponse getMenuDetail(Long menuId) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new CustomException(ErrorCode.MENU_NOT_FOUND));

        List<MenuOptionResponse> menuOptions = menu.getMenuOptions().stream()
                .map(MenuOptionResponse::from)
                .toList();

        return MenuDetailResponse.from(menu, menuOptions);
    }
}
