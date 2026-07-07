package mutsa_vacation_week1.demo.menuOption.dto;

public record MenuOptionDeleteResponse(
        String message,
        Long id
) {
    public static MenuOptionDeleteResponse of(Long id) {
        return new MenuOptionDeleteResponse("옵션이 삭제되었습니다.", id);
    }
}
