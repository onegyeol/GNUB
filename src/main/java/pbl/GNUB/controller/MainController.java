package pbl.GNUB.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import lombok.extern.slf4j.Slf4j;
import pbl.GNUB.entity.Shop;
import pbl.GNUB.entity.ShopMenu;
import pbl.GNUB.service.ShopService;
import pbl.GNUB.service.ShopTagMappingService;

@Slf4j
@Controller
public class MainController {

    private final JobLauncher jobLauncher;
    private final Job csvShopJob; 
    private final ShopService shopService;
    private final TagController tagController;
    private final ShopTagMappingService mappingService;

    @Autowired
    public MainController(JobLauncher jobLauncher, Job csvShopJob, 
                        JobRepository jobRepository, ShopService shopService, 
                        TagController tagController, ShopTagMappingService mappingService) {
        this.jobLauncher = jobLauncher;
        this.csvShopJob = csvShopJob;
        this.shopService = shopService;
        this.tagController = tagController;
        this.mappingService = mappingService;
    }

    @GetMapping("/main")
    public String showMainPage(Model model) {

        List<Shop> shops = shopService.getAllShops();

        Map<String, List<Shop>> categorizedShops = new HashMap<>();
    
        for (Shop shop : shops) {
            String category = (shop.getCategory() == null || shop.getCategory().isEmpty()) ? "기타" : shop.getCategory();
            categorizedShops.computeIfAbsent(category, k -> new ArrayList<>()).add(shop);
        }
    
        model.addAttribute("categorizedShopsList", categorizedShops.entrySet());

        
        /* ShopTag.csv 파일 완성 시 까지 주석 처리
         * 
         * // 1. 먼저 매핑을 수행 (이름 기준)
        // mappingService.mapShopAndShopTagsByName();

        // 2. 매핑된 데이터를 기반으로 태그 맵 가져오기
        Map<String, List<String>> shopTagsMap = tagController.getShopTagsMap();
        mappingService.mapShopAndShopTagsById(); // shop과 shopTag id 매핑

        // 3. 좋아요 기준 상위 28개 Shop 가져오기
        List<Shop> shops = shopService.getTop28ShopsByLikes();

        
        HashMap<String, List<Shop>> taggedShops = new HashMap<>();

        for (Shop shop : shops) {
            List<String> shopTags = shopTagsMap.get(shop.getName());
            if (shopTags != null) {
                for (String tag : shopTags) {
                    taggedShops.computeIfAbsent(tag, k -> new ArrayList<>()).add(shop);
                }
            }
        }

        model.addAttribute("shops", shops);
        model.addAttribute("shopTagsMap", shopTagsMap); // 이 부분도 추가함
        model.addAttribute("taggedShops", taggedShops);
         * 
         */
        

        return "form/main";
    }

    @GetMapping("/springBatch")
    public String startBatchJob() {
        try {
            JobParameters params = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis()) // 매번 다른 값으로 전달
                    .toJobParameters();
            jobLauncher.run(csvShopJob, params); 
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "form/main";
    }

    @GetMapping("/springBatchManual")
    public String startBatchJobManually() {
        try {
            JobParameters params = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();

            // 직접 Job 인스턴스를 가져와서 실행
            JobExecution jobExecution = jobLauncher.run(csvShopJob, params);
            log.info("🟣 Job Execution Status: {}", jobExecution.getStatus());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "form/main";
    }


    @GetMapping("/shopDetails/{id}") // 음식점 상세 페이지
    public String foodDetailsPage(@PathVariable("id") Long id, Model model) {
        Shop shop = shopService.findShopById(id);
        List<ShopMenu> shopMenus = shopService.getMenusByShopName(shop.getName());

        if (shop != null) {
            System.out.println("🔴 shopMenus: " + shopMenus);
            model.addAttribute("shop", shop);
            model.addAttribute("shopMenus", shopMenus);
            return "form/foodDetails";

        } else {
            return "error";
        }
    }

    // GPT 화면
    @GetMapping("/ask")
    public String GptPage() {
        return "form/recommend"; // 로그인 폼 뷰를 반환
    }
}
