package mutsa_vacation_week1.demo.menuOption;

import lombok.RequiredArgsConstructor;
import mutsa_vacation_week1.demo.menuOption.dto.MenuOptionCreateRequest;
import mutsa_vacation_week1.demo.menuOption.dto.MenuOptionDeleteResponse;
import mutsa_vacation_week1.demo.menuOption.dto.MenuOptionResponse;
import mutsa_vacation_week1.demo.menuOption.dto.MenuOptionUpdateRequest;
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
    public MenuOptionResponse createOption(@PathVariable Long menuId,
                                            @RequestBody MenuOptionCreateRequest request) {
        return menuOptionService.createOption(menuId, request);
    }

    @PatchMapping("/menuOption/{menuOptionId}")
    public MenuOptionResponse updateOption(@PathVariable Long menuOptionId,
                                            @RequestBody MenuOptionUpdateRequest request) {
        return menuOptionService.updateOption(menuOptionId, request);
    }

    @DeleteMapping("/menuOption/{menuOptionId}")
    public MenuOptionDeleteResponse deleteOption(@PathVariable Long menuOptionId) {
        return menuOptionService.deleteOption(menuOptionId);
    }
}
