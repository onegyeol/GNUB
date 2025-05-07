package pbl.GNUB.controller.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pbl.GNUB.dto.ShopDto;
import pbl.GNUB.entity.Shop;
import pbl.GNUB.service.ShopService;
import pbl.GNUB.service.TagService;

@RestController
@RequestMapping("/api/main")
public class MainApiController {

    private final ShopService shopService;
    private final TagService tagService;

    public MainApiController(ShopService shopService, TagService tagService) {
        this.shopService = shopService;
        this.tagService = tagService;
    }

    @GetMapping("/tags")
    public Map<String, Object> getTaggedShops() {
        Map<String, List<Shop>> tagMap = tagService.getAllTaggedShopsTop100();

        Map<String, Object> response = new HashMap<>();
        Map<String, List<ShopDto>> taggedShops = new HashMap<>();
        int uniqueCount = 0;

        Set<Long> uniqueIds = new HashSet();
        for (Map.Entry<String, List<Shop>> entry : tagMap.entrySet()) {
            List<ShopDto> dtos = new ArrayList<>();
            for (Shop shop : entry.getValue()) {
                dtos.add(new ShopDto(shop));
                uniqueIds.add(shop.getId());
            }
            taggedShops.put(entry.getKey(), dtos);
        }

        uniqueCount = uniqueIds.size();
        response.put("taggedShops", taggedShops);
        response.put("uniqueShopCount", uniqueCount);

        return response;
    }
}
