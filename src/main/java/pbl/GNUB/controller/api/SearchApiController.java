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
                "query", query);
    }

    @GetMapping("/search/{tag}")
    public Map<String, Object> searchShopsByTag(
            @PathVariable("tag") String tag,
            @RequestParam(value = "menu", required = false) String menu) {

        List<Shop> shops = tagService.getTop100ShopsByTag(tag);

        // ✅ 메뉴 먼저 세팅
        Map<Long, List<ShopMenu>> menuMap = shopService.getMenusForShops(shops);
        for (Shop shop : shops) {
            shop.setShopMenus(menuMap.getOrDefault(shop.getId(), List.of()));
        }

        // ✅ 이후 필터링
        if (menu != null && !menu.isBlank()) {
            shops = shops.stream()
                    .filter(shop -> shop.getShopMenus().stream()
                            .anyMatch(m -> m.getMenuName() != null && m.getMenuName().contains(menu)))
                    .collect(Collectors.toList());
        }

        return Map.of(
                "shops", shops,
                "query", tag);
    }

}
