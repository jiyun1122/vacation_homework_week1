package mutsa_vacation_week1.demo.store.dto.response;


import java.util.List;

public record StoreListResponse(
        List<StoreSummaryResponse> stores
) {
}