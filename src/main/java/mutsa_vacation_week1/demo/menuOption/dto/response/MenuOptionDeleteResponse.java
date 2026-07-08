package mutsa_vacation_week1.demo.menuOption.dto.response;

import mutsa_vacation_week1.demo.global.apiPayload.code.SuccessCode;

public record MenuOptionDeleteResponse(
        String message,
        Long id
) {
    public static MenuOptionDeleteResponse of(Long id) {
        return new MenuOptionDeleteResponse(SuccessCode.MENU_OPTION_DELETED.getMessage(), id);
    }
}
