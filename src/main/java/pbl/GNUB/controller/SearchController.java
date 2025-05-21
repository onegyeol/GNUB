package pbl.GNUB.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import pbl.GNUB.entity.Shop;
import pbl.GNUB.entity.ShopMenu;
import pbl.GNUB.entity.ShopTag;
import pbl.GNUB.service.ShopService;
import pbl.GNUB.service.TagMappingService;
import pbl.GNUB.service.TagService;

@Controller
@RequiredArgsConstructor // final 필드에 대한 생성자를 자동으로 생성
public class SearchController {
    private final TagController tagController;
    private final ShopService shopService;
    private final TagService tagService;
    private final TagMappingService tagMappingService;

    @GetMapping("/search")
    public String searchPage(@RequestParam(value = "query", required = false) String query, Model model) {
        List<Shop> shops;

        if (query != null && !query.trim().isEmpty()) {
            // 메뉴 기준으로 음식점 검색
            shops = shopService.searchShopsByMenuKeyword(query);
        } else {
            shops = shopService.getAllShops(); // 검색어 없으면 전체 조회
        }

        model.addAttribute("shops", shops);
        model.addAttribute("query", query);
        return "form/search";
    }

    @GetMapping("/search/{tag}")
    public String searchByTag(@PathVariable("tag") String tag, Model model) {
        List<Shop> shops = tagService.getTop100ShopsByTag(tag);

        Map<Long, List<ShopMenu>> menuMap = shopService.getMenusForShops(shops);
        for (Shop shop : shops) {
            shop.setShopMenus(menuMap.getOrDefault(shop.getId(), List.of()));
        }

        model.addAttribute("shops", shops);
        model.addAttribute("query", tag);
        return "form/search";
    }

    /*
     * 기존 태그들을 못써서 일단 주석
     * 
     * @GetMapping("/search/{tag}")
     * public String getShopsByTag(
     * 
     * @PathVariable("tag") String tag,
     * 
     * @RequestParam(value = "query", required = false, defaultValue = "") String
     * query,
     * Model model) {
     * 
     * // 태그를 영어로 변환
     * String englishTag = tagMappingService.toEnglish(tag);
     * 
     * // 태그와 검색어를 조합하여 필터링
     * List<Shop> shops = shopService.getShopsByTagField(englishTag, query);
     * 
     * //Map<String, List<String>> tags = tagController.getShopTagsMap();
     * Map<Long, List<String>> shopTagsMap = shops.stream()
     * .collect(Collectors.toMap(
     * Shop::getId,
     * shop -> shop.getShopTags().stream()
     * .map(ShopTag::getName)
     * .collect(Collectors.toList())
     * ));
     * 
     * model.addAttribute("shops", shops);
     * model.addAttribute("query", query);
     * model.addAttribute("shopTagsMap", shopTagsMap);
     * model.addAttribute("selectedTag", tag);
     * //model.addAttribute("tags", tags);
     * 
     * return "form/search";
     * }
     */

}
