package mutsa_vacation_week1.demo.store.service;

import lombok.RequiredArgsConstructor;
import mutsa_vacation_week1.demo.store.dto.response.StoreListResponse;
import mutsa_vacation_week1.demo.store.dto.response.StoreSummaryResponse;
import mutsa_vacation_week1.demo.store.entity.Store;
import mutsa_vacation_week1.demo.store.repository.StoreRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class    StoreService {

    private final StoreRepository storeRepository;

    public StoreListResponse getStoreList(String category) {
        List<Store> storeEntities;

        if (category == null || category.isBlank() || category.equals("전체")) {
            storeEntities = storeRepository.findAll();
        } else {
            storeEntities = storeRepository.findAllByCategory(category);
        }

        List<StoreSummaryResponse> stores = storeRepository.findAll().stream()
                .map(StoreSummaryResponse::from)
                .toList();

        return new StoreListResponse(stores);
    }
}
