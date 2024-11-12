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
    List<Shop> shops = shopService.getTop30Shops();
    Map<String, ShopTag> shopTags = tagController.getShopTagsMap();
    log.info("shopTags: {}", shopTags);
    model.addAttribute("shops", shops);
    model.addAttribute("shopTags", shopTags);
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
    public String searchPage() {
        return "form/search";
    }

    @GetMapping("/shopDetails")
    public String foodDetailsPage() {
        return "form/foodDetails";
    }
}