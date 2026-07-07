package mutsa_vacation_week1.demo.menuOption;

import lombok.RequiredArgsConstructor;
import mutsa_vacation_week1.demo.global.exception.CustomException;
import mutsa_vacation_week1.demo.global.exception.ErrorCode;
import mutsa_vacation_week1.demo.menu.Menu;
import mutsa_vacation_week1.demo.menu.MenuRepository;
import mutsa_vacation_week1.demo.menuOption.dto.MenuOptionCreateRequest;
import mutsa_vacation_week1.demo.menuOption.dto.MenuOptionDeleteResponse;
import mutsa_vacation_week1.demo.menuOption.dto.MenuOptionResponse;
import mutsa_vacation_week1.demo.menuOption.dto.MenuOptionUpdateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuOptionService {

    private final MenuOptionRepository menuOptionRepository;
    private final MenuRepository menuRepository;

    @Transactional
    public MenuOptionResponse createOption(Long menuId, MenuOptionCreateRequest request) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new CustomException(ErrorCode.MENU_NOT_FOUND));

        MenuOption menuOption = MenuOption.builder()
                .menu(menu)
                .option(request.option())
                .content(request.content())
                .price(request.price())
                .build();

        MenuOption savedMenuOption = menuOptionRepository.save(menuOption);

        return MenuOptionResponse.from(savedMenuOption);
    }

    @Transactional
    public MenuOptionResponse updateOption(Long menuOptionId, MenuOptionUpdateRequest request) {
        MenuOption menuOption = menuOptionRepository.findById(menuOptionId)
                .orElseThrow(() -> new CustomException(ErrorCode.MENU_OPTION_NOT_FOUND));

        menuOption.update(request.content(), request.price());

        return MenuOptionResponse.from(menuOption);
    }

    @Transactional
    public MenuOptionDeleteResponse deleteOption(Long menuOptionId) {
        MenuOption menuOption = menuOptionRepository.findById(menuOptionId)
                .orElseThrow(() -> new CustomException(ErrorCode.MENU_OPTION_NOT_FOUND));

        menuOptionRepository.delete(menuOption);

        return MenuOptionDeleteResponse.of(menuOptionId);
    }
}
