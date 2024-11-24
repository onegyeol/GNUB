package pbl.GNUB.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;
import pbl.GNUB.dto.ShopDto;
import pbl.GNUB.dto.ShopTagDto;
import pbl.GNUB.entity.Shop;
import pbl.GNUB.entity.ShopTag;
import pbl.GNUB.entity.TagMapping;
import pbl.GNUB.service.ShopService;
import pbl.GNUB.service.ShopTagMappingService;
import pbl.GNUB.service.ShopTagService;
import pbl.GNUB.service.TagMappingService;

@Slf4j
@Controller
public class MainController {

    private final JobLauncher jobLauncher;
    private final Job csvShopJob;
    private final ShopService shopService;
    private final TagController tagController;
    private final ShopTagService shopTagService;
    private final ShopTagMappingService mappingService;
    private final TagMappingService tagMappingService;


    @Autowired
    public MainController(JobLauncher jobLauncher, Job csvShopJob, ShopService shopService, TagController tagController, 
                        ShopTagService shopTagService, ShopTagMappingService mappingService, TagMappingService tagMappingService) {
        this.jobLauncher = jobLauncher;
        this.csvShopJob = csvShopJob;
        this.shopService = shopService;
        this.tagController = tagController;
        this.shopTagService = shopTagService;
        this.mappingService = mappingService;
        this.tagMappingService = tagMappingService;
    } 

    @GetMapping("/main")
    public String showMainPage(Model model) {
        List<Shop> shops = shopService.getTop28ShopsByLikes(); // 좋아요 기준 상위 28개 조회

        //태그 컨트롤러로 매핑하는거 추가함
        Map<String, List<String>> shopTagsMap = tagController.getShopTagsMap();
        //mappingService.mapShopAndShopTagsById(); // shop과 shopTag id 매핑
        
        model.addAttribute("shops", shops);
        model.addAttribute("shopTagsMap", shopTagsMap); //이것도 추가함
        return "form/main";
    }

    @GetMapping("/springBatch")
    public String startBatchJob() {
        try {
            JobParameters params = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();
            jobLauncher.run(csvShopJob, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "form/main";
    }
    
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


    @GetMapping("/shopDetails/{id}")// 음식점상세 페이지
    public String foodDetailsPage(@PathVariable("id") Long id, Model model) {
        Shop shop = shopService.findShopById(id);
        if(shop != null){
            model.addAttribute("shop", shop);
            return "form/foodDetails";
        }else{
            return "error";
        }
    }
}