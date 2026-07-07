package mutsa_vacation_week1.demo.menuOption;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
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
                .orElseThrow(() -> new EntityNotFoundException("메뉴를 찾을 수 없습니다. id=" + menuId));

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
                .orElseThrow(() -> new EntityNotFoundException("옵션을 찾을 수 없습니다. id=" + menuOptionId));

        menuOption.update(request.content(), request.price());

        return MenuOptionResponse.from(menuOption);
    }

    @Transactional
    public MenuOptionDeleteResponse deleteOption(Long menuOptionId) {
        MenuOption menuOption = menuOptionRepository.findById(menuOptionId)
                .orElseThrow(() -> new EntityNotFoundException("옵션을 찾을 수 없습니다. id=" + menuOptionId));

        menuOptionRepository.delete(menuOption);

        return MenuOptionDeleteResponse.of(menuOptionId);
    }
}
