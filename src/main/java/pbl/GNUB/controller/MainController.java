package pbl.GNUB.controller;

import java.util.List;

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

import pbl.GNUB.entity.Shop;
import pbl.GNUB.service.ShopService;


@Controller
public class MainController {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job csvShopJob; // Job 이름에 맞게 수정하세요.

    @Autowired
    private ShopService shopService;

    @GetMapping("/main")
    public String showMainPage(Model model) {
        List<Shop> shops = shopService.getTop30Shops(); // 데이터 가져오기
        model.addAttribute("shops", shops); // 모델에 데이터 추가
        return "form/main"; // 반환할 뷰 이름
    }
    

    @GetMapping("/springBatch")
    public String startBatchJob() {
        try {
            // JobParameters를 생성합니다. 필요한 경우 추가 파라미터를 설정할 수 있습니다.
            JobParameters params = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();
            
            // Job을 실행합니다.
            jobLauncher.run(csvShopJob, params);
        } catch (Exception e) {
            e.printStackTrace();
            // 에러 처리
        }
        return "form/main"; // 반환할 뷰 이름
    }
    
    @GetMapping("/search") // 검색결과 페이지
    public String searchPage() {
        return "form/search";
    }

    @GetMapping("/shopDetails/{id}") // 음식점상세 페이지
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