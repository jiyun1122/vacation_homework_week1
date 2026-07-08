package mutsa_vacation_week1.demo.menu.controller;

import lombok.RequiredArgsConstructor;
import mutsa_vacation_week1.demo.global.apiPayload.ApiResponse;
import mutsa_vacation_week1.demo.menu.dto.response.MenuDetailResponse;
import mutsa_vacation_week1.demo.menu.dto.response.MenuListResponse;
import mutsa_vacation_week1.demo.menu.service.MenuService;
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
    public ApiResponse<MenuListResponse> getMenuList() {
        return ApiResponse.onSuccess("메뉴 목록 조회 성공", menuService.getMenuList());
    }

    @GetMapping("/{menuId}")
    public ApiResponse<MenuDetailResponse> getMenuDetail(@PathVariable Long menuId) {
        return ApiResponse.onSuccess("메뉴 상세 조회 성공", menuService.getMenuDetail(menuId));
    }
}
