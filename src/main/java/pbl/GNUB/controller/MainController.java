package pbl.GNUB.controller;

import java.util.List;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.extern.slf4j.Slf4j;
import pbl.GNUB.entity.Shop;
import pbl.GNUB.entity.ShopTag;
import pbl.GNUB.service.ShopService;

@Slf4j
@Controller
public class MainController {

    private final JobLauncher jobLauncher;
    private final Job csvShopJob;
    private final ShopService shopService;
    private final TagController tagController;

    @Autowired
    public MainController(JobLauncher jobLauncher, Job csvShopJob, ShopService shopService, TagController tagController) {
        this.jobLauncher = jobLauncher;
        this.csvShopJob = csvShopJob;
        this.shopService = shopService;
        this.tagController = tagController;
    }

    @GetMapping("/main")
    public String showMainPage(Model model) {
        // ㅔㅁ인 화면에 음식점 정보 30개 띄우기 위함
        List<Shop> shops = shopService.getTop30Shops();
        //태그 컨트롤러로 매핑하는거 추가함
        Map<String, List<String>> shopTagsMap = tagController.getShopTagsMap();
        
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
        System.out.println("검색어: " + query);  // 디버깅용 로그 추가
        
        // 검색어가 있을 경우
        List<Shop> shops = shopService.searchShops(query);
        model.addAttribute("shops", shops);
    
        return "form/search";  // 결과를 보여줄 뷰 반환
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