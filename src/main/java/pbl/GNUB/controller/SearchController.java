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
import pbl.GNUB.entity.ShopTag;
import pbl.GNUB.service.ShopService;
import pbl.GNUB.service.TagMappingService;

@Controller
@RequiredArgsConstructor // final 필드에 대한 생성자를 자동으로 생성
public class SearchController {
    private final TagController tagController;
    private final ShopService shopService;
    private final TagMappingService tagMappingService;

    @GetMapping("/search")
    public String searchPage(@RequestParam(value = "query", required = false) String query, Model model) {
        //태그 컨트롤러로 매핑하는거 추가함
        Map<String, List<String>> tags = tagController.getShopTagsMap();
        
        // 검색어가 있을 경우
        List<Shop> shops = shopService.searchShops(query);
        
        model.addAttribute("shops", shops);
        model.addAttribute("query", query);
        model.addAttribute("tags", tags); //이것도 추가함
        
        return "form/search";  // 결과를 보여줄 뷰 반환
    }

    @GetMapping("/search/{tag}")
    public String getShopsByTag(
            @PathVariable("tag") String tag,
            @RequestParam(value = "query", required = false, defaultValue = "") String query,
            Model model) {

        // 태그를 영어로 변환
        String englishTag = tagMappingService.toEnglish(tag);

        // 태그와 검색어를 조합하여 필터링
        List<Shop> shops = shopService.getShopsByTagField(englishTag, query);

        Map<String, List<String>> tags = tagController.getShopTagsMap();
        Map<Long, List<String>> shopTagsMap = shops.stream()
                .collect(Collectors.toMap(
                        Shop::getId,
                        shop -> shop.getShopTags().stream()
                                    .map(ShopTag::getName)
                                    .collect(Collectors.toList())
                ));

        model.addAttribute("shops", shops);
        model.addAttribute("query", query);
        model.addAttribute("shopTagsMap", shopTagsMap);
        model.addAttribute("selectedTag", tag);
        model.addAttribute("tags", tags);

        return "form/search";
    }
    
    
}
