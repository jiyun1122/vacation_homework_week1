package mutsa_vacation_week1.demo.menuOption.dto.request;

public record MenuOptionCreateRequest(
        String option,
        String content,
        Integer price
) {
}
