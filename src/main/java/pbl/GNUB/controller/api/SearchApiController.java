package pbl.GNUB.controller.api;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pbl.GNUB.entity.Shop;
import pbl.GNUB.entity.ShopMenu;
import pbl.GNUB.service.ShopService;
import pbl.GNUB.service.TagMappingService;
import pbl.GNUB.service.TagService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SearchApiController {
    
    private final ShopService shopService;
    private final TagMappingService tagMappingService;
    private final TagService tagService;

    @GetMapping("/search")
    public Map<String, Object> searchShops(
            @RequestParam(value = "query", required = false) String query) {
        
        List<Shop> shops = shopService.searchShops(query);

        return Map.of(
            "shops", shops,
            "query", query
        );
    }

    @GetMapping("/search/{tag}")
    public Map<String, Object> searchShopsByTag(@PathVariable("tag") String tag) {
        List<Shop> shops = tagService.getTop100ShopsByTag(tag);

        Map<Long, List<ShopMenu>> menuMap = shopService.getMenusForShops(shops);
        for (Shop shop : shops) {
            shop.setShopMenus(menuMap.getOrDefault(shop.getId(), List.of()));
        }

        return Map.of(
            "shops", shops,
            "query", tag
        );
    }
}

