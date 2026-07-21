package mutsa_vacation_week1.demo.store.controller;

import lombok.RequiredArgsConstructor;
import mutsa_vacation_week1.demo.global.apiPayload.ApiResponse;
import mutsa_vacation_week1.demo.store.dto.response.StoreListResponse;
import mutsa_vacation_week1.demo.store.service.StoreService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @GetMapping
    public ApiResponse<StoreListResponse> getStoreList(
            @RequestParam(required = false) String category
    ) {
        return ApiResponse.onSuccess("가게 목록 조회 성공", storeService.getStoreList(category));
    }
}
