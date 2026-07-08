package mutsa_vacation_week1.demo.global.response;

import lombok.Getter;

@Getter
public enum SuccessCode {
    MENU_OPTION_DELETED("옵션이 삭제되었습니다.");

    private final String message;

    SuccessCode(String message) {
        this.message = message;
    }
}
