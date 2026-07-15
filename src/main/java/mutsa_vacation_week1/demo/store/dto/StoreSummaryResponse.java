package mutsa_vacation_week1.demo.store.dto;

import mutsa_vacation_week1.demo.store.entity.Store;

import java.math.BigDecimal;

public record StoreSummaryResponse(
        Long id,
        String name,
        String address,
        String thumbnail,
        String category,
        BigDecimal rating
) {
    public static StoreSummaryResponse from(Store store) {
        return new StoreSummaryResponse(
                store.getId(),
                store.getName(),
                store.getAddress(),
                store.getThumbnail(),
                store.getCategory(),
                store.getRating()
        );
    }
}