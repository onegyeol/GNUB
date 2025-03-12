package pbl.GNUB.controller;

import java.util.ArrayList;
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
    private final ShopTagMappingService mappingService;

    @Autowired
    public MainController(JobLauncher jobLauncher, Job csvShopJob, ShopService shopService, TagController tagController, ShopTagMappingService mappingService) {
        this.jobLauncher = jobLauncher;
        this.csvShopJob = csvShopJob;
        this.shopService = shopService;
        this.tagController = tagController;
        this.mappingService = mappingService;
    } 

    @GetMapping("/main")
    public String showMainPage(Model model) {
        List<Shop> shops = shopService.getTop28ShopsByLikes(); // 좋아요 기준 상위 28개 조회

        //태그 컨트롤러로 매핑하는거 추가함
        Map<String, List<String>> shopTagsMap = tagController.getShopTagsMap();
        mappingService.mapShopAndShopTagsById(); // shop과 shopTag id 매핑
        
        HashMap<String, List<Shop>> taggedShops =new HashMap<>();

        for(Shop shop : shops) {
            List<String> shopTags =shopTagsMap.get(shop.getName());
            if(shopTags !=null) {
                for(String tag :shopTags) {
                    taggedShops.computeIfAbsent(tag, k ->new ArrayList<>()).add(shop);
                }
            }
        }

        model.addAttribute("shops", shops);
        model.addAttribute("shopTagsMap", shopTagsMap); //이것도 추가함
        model.addAttribute("taggedShops", taggedShops);
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

    // gpt 화면
    @GetMapping("/ask")
    public String GptPage(){
        return "form/recommend"; // 로그인 폼 뷰를 반환
    }
}