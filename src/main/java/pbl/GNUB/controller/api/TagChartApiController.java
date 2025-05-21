package pbl.GNUB.controller.api;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import pbl.GNUB.entity.Shop;
import pbl.GNUB.repository.ShopRepository;
import pbl.GNUB.service.TagService;

import java.util.Map;

@RestController
@RequestMapping("/api/chart")
public class TagChartApiController {

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private TagService tagService;

    @GetMapping("/{shopId}")
    public Map<String, Integer> getRadarChart(@PathVariable Long shopId) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new RuntimeException("No shop"));
        return tagService.getRadarChartData(shop);
    }
}