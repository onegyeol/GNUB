package pbl.GNUB.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pbl.GNUB.entity.Shop;
import pbl.GNUB.repository.ShopRepository;

@Service
public class ShopService {
    @Autowired
    private ShopRepository shopRepository;

    public List<Shop> getTop30Shops() {
        // id가 1부터 30까지인 데이터를 불러오기
        return shopRepository.findByIdBetween(1L, 30L);
    }
    public Shop findShopById(Long id){
        return shopRepository.findById(id).orElse(null);
    }

    public List<Shop> searchShops(String query) {
        if (query == null || query.trim().isEmpty()) {
            return shopRepository.findAll();  // 검색어가 없으면 전체 목록 반환
        }
        
        // 음식점명, 주메뉴, 태그 이름으로 검색
        return shopRepository.searchShops(query);  // 동일한 값으로 세 가지 필드 검색
    }
}