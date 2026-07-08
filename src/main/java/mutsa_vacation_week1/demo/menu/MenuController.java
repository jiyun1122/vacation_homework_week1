package mutsa_vacation_week1.demo.menu;

import lombok.RequiredArgsConstructor;
import mutsa_vacation_week1.demo.menu.dto.MenuDetailResponse;
import mutsa_vacation_week1.demo.menu.dto.MenuListResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/menu")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @GetMapping
    public MenuListResponse getMenuList() {
        return menuService.getMenuList();
    }

    @GetMapping("/{menuId}")
    public MenuDetailResponse getMenuDetail(@PathVariable Long menuId) {
        return menuService.getMenuDetail(menuId);
    }
}
