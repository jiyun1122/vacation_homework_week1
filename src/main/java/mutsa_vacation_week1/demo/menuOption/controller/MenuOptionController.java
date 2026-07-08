package mutsa_vacation_week1.demo.menuOption.controller;

import lombok.RequiredArgsConstructor;
import mutsa_vacation_week1.demo.global.apiPayload.ApiResponse;
import mutsa_vacation_week1.demo.menuOption.dto.request.MenuOptionCreateRequest;
import mutsa_vacation_week1.demo.menuOption.dto.request.MenuOptionUpdateRequest;
import mutsa_vacation_week1.demo.menuOption.dto.response.MenuOptionDeleteResponse;
import mutsa_vacation_week1.demo.menuOption.dto.response.MenuOptionResponse;
import mutsa_vacation_week1.demo.menuOption.service.MenuOptionService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MenuOptionController {

    private final MenuOptionService menuOptionService;

    @PostMapping("/menu/{menuId}/menuOption")
    public ApiResponse<MenuOptionResponse> createOption(@PathVariable Long menuId,
                                            @RequestBody MenuOptionCreateRequest request) {
        return ApiResponse.onSuccess("옵션 생성 성공", menuOptionService.createOption(menuId, request));
    }

    @PatchMapping("/menuOption/{menuOptionId}")
    public ApiResponse<MenuOptionResponse> updateOption(@PathVariable Long menuOptionId,
                                            @RequestBody MenuOptionUpdateRequest request) {
        return ApiResponse.onSuccess("옵션 수정 성공", menuOptionService.updateOption(menuOptionId, request));
    }

    @DeleteMapping("/menuOption/{menuOptionId}")
    public ApiResponse<MenuOptionDeleteResponse> deleteOption(@PathVariable Long menuOptionId) {
        return ApiResponse.onSuccess("옵션 삭제 성공", menuOptionService.deleteOption(menuOptionId));
    }
}
