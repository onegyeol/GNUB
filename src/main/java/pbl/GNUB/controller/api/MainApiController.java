package pbl.GNUB.controller.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pbl.GNUB.dto.ShopDto;
import pbl.GNUB.entity.Shop;
import pbl.GNUB.service.ShopService;

@RestController
@RequestMapping("/api/main")
public class MainApiController {

    private final ShopService shopService;

    public MainApiController(ShopService shopService) {
        this.shopService = shopService;
    }

    // main 화면에서 쓰일 음식점 데이터 넘겨받음
    @GetMapping("/shops")
    public Map<String, List<ShopDto>> getCategorizedShops() {
        List<Shop> shops = shopService.getAllShops();
    
        Map<String, List<ShopDto>> categorizedShops = new HashMap<>();
        for (Shop shop : shops) {
            String category = (shop.getCategory() == null || shop.getCategory().isEmpty()) ? "기타" : shop.getCategory();
            categorizedShops.computeIfAbsent(category, k -> new ArrayList<>()).add(new ShopDto(shop));
        }
    
        return categorizedShops;
    }
    

}
