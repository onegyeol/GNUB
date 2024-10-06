package pbl.GNUB.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job csvShopJob; // Job 이름에 맞게 수정하세요.

    @GetMapping("/main")
    public String homePage() {
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

    @GetMapping("/shopDetails") // 음식점상세 페이지
    public String foodDetailsPage() {
        return "form/foodDetails";
    }
    
    
}
