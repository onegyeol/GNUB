package pbl.GNUB.controller.api;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pbl.GNUB.entity.Shop;
import pbl.GNUB.service.ShopService;
import pbl.GNUB.service.TagMappingService;

@RestController
@RequiredArgsConstructor
public class SearchApiController {
    
    private final ShopService shopService;
    private final TagMappingService tagMappingService;

    @GetMapping("/api/search")
    public Map<String, Object> searchShops(
            @RequestParam(value = "query", required = false) String query) {
        
        List<Shop> shops = shopService.searchShops(query);

        return Map.of(
            "shops", shops,
            "query", query
        );
    }
}

