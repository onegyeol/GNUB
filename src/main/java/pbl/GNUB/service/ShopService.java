package pbl.GNUB.service;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import pbl.GNUB.entity.Shop;
import pbl.GNUB.entity.ShopMenu;
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

    // 1. 좋아요 순으로 상위 28개 음식점
    public List<Shop> getTop28ShopsByLikes() {
        return shopRepository.findTop28ByOrderByLikeCountDesc();
    }

    // 2. 전체 음식점
    public List<Shop> getAllShops() {
        return shopRepository.findAll();
    }

    // 3. 지도 범위 내 음식점
    public List<Map<String, Object>> getShopsByBounds(Double neLat, Double neLng, Double swLat, Double swLng){
        List<Shop> shops = shopRepository.findShopsByBounds(swLat, neLat, swLng, neLng);

        return shops.stream().map(shop -> {
            Map<String, Object> shopData = new HashMap<>();
            shopData.put("id", shop.getId());
            shopData.put("name", shop.getName());
            shopData.put("address", shop.getAddress());
            shopData.put("lat", shop.getLat());
            shopData.put("lng", shop.getLng());
            return shopData;
        }).collect(Collectors.toList());
    }

    // 4. ID로 음식점 조회
    public Shop findShopById(Long id){
        return shopRepository.findById(id).orElse(null);
    }

    // 5. 음식점 이름 기반 검색
    public List<Shop> searchShops(String query) {
        if (query == null || query.trim().isEmpty()) {
            return shopRepository.findAll();  // 검색어 없으면 전체
        }
        return shopRepository.searchShops(query); // 이름, 주소, 주메뉴 등에 대해 검색
    }

    // 6. 메뉴명으로 음식점 검색
    public List<Shop> findShopsByMenuName(String keyword) {
        List<ShopMenu> menus = shopMenuRepository.findByMenuNameContainingIgnoreCase(keyword);
        Set<String> restaurantNames = menus.stream()
            .map(ShopMenu::getRestName) // rest_name 필드
            .collect(Collectors.toSet());
    
        // rest_name → Shop.name 으로 매핑
        return shopRepository.findByNameIn(restaurantNames);
    }
    
    // 7. 음식점 + 메뉴 통합 검색
    public List<Shop> searchShopsIncludingMenu(String keyword) {
        Set<Shop> result = new HashSet<>();
        result.addAll(searchShops(keyword));              // 음식점 이름, 주소, 주메뉴 등
        result.addAll(findShopsByMenuName(keyword));      // 메뉴명으로 검색한 음식점
        return new ArrayList<>(result);
    }

    public List<ShopMenu> getMenusByShopName(String shopName) {
        return shopMenuRepository.findByRestName(shopName);
    }
    
}
