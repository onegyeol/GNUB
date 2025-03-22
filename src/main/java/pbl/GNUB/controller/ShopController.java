package pbl.GNUB.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pbl.GNUB.entity.Shop;
import pbl.GNUB.service.ShopService;

import java.util.List;
import java.util.Map;

@RestController // JSON 반환을 위한 @RestController 사용
@RequestMapping("/api/shops")
public class ShopController {

    private final ShopService shopService;

    public ShopController(ShopService shopService) {
        this.shopService = shopService;
    }

    @GetMapping
    public List<Map<String, Object>> getShopsInBounds(
        @RequestParam Double neLat, 
        @RequestParam Double neLng,
        @RequestParam Double swLat, 
        @RequestParam Double swLng
    ) {
        System.out.println("🔴 범위: NE(" + neLat + ", " + neLng + "), SW(" + swLat + ", " + swLng + ")");
    
        return shopService.getShopsByBounds(neLat, neLng, swLat, swLng);
    }
}
