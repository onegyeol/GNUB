package pbl.GNUB.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pbl.GNUB.dto.ShopDto;
import pbl.GNUB.entity.Shop;
import pbl.GNUB.entity.ShopMenu;
import pbl.GNUB.entity.TagMapping;
import pbl.GNUB.repository.ShopMenuRepository;
import pbl.GNUB.repository.ShopRepository;

@Service
public class ShopService {

    private final ShopRepository shopRepository;
    private final ShopMenuRepository shopMenuRepository;

    public ShopService(ShopRepository shopRepository, ShopMenuRepository shopMenuRepository){
        this.shopRepository = shopRepository;
        this.shopMenuRepository = shopMenuRepository;
    }
    
    // 좋아요 순으로 상위 28개 음식점 가져오기
    public List<Shop> getTop28ShopsByLikes() {
        return shopRepository.findTop28ByOrderByLikeCountDesc();
    }

    
    public List<Shop> getAllShops() {
        return shopRepository.findAll();
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

    public List<Shop> getShopsByTagField(String tag, String query) {
        return shopRepository.findShopsByDynamicTag(tag, query);
    }

    public List<ShopMenu> getMenusByShopName(String shopName) {
        return shopMenuRepository.findByRestName(shopName);
    }
}