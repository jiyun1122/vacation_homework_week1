package mutsa_vacation_week1.demo.store.controller;

import lombok.RequiredArgsConstructor;
import mutsa_vacation_week1.demo.store.dto.response.StoreListResponse;
import mutsa_vacation_week1.demo.store.dto.response.StoreSummaryResponse;
import mutsa_vacation_week1.demo.store.entity.Store;
import mutsa_vacation_week1.demo.store.repository.StoreRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;

    public StoreListResponse getStoreList() {
        List<StoreSummaryResponse> stores = storeRepository.findAll().stream()
                .map(StoreSummaryResponse::from)
                .toList();

        return new StoreListResponse(stores);
    }
}
