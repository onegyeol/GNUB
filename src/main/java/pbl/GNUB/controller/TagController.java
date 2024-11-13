package pbl.GNUB.controller;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;
import pbl.GNUB.entity.ShopTag;
import pbl.GNUB.repository.ShopTagRepository;

@Slf4j
@Controller
public class TagController {

    private final JobLauncher jobLauncher;
    private final Job csvShopTagJob;
    private final ShopTagRepository shopTagRepository;

    @Autowired
    public TagController(JobLauncher jobLauncher, Job csvShopTagJob, ShopTagRepository shopTagRepository) {
        this.jobLauncher = jobLauncher;
        this.csvShopTagJob = csvShopTagJob;
        this.shopTagRepository = shopTagRepository;
    }

    /*@GetMapping("/tags")
    public String showTags(Model model) {
        List<ShopTag> shopTags = shopTagRepository.findByValue(1);
        model.addAttribute("shopTags", shopTags);
        return "form/main";
    }*/

    @GetMapping("/importTags")
    public String importTagsFromCsv() {
        try {
            JobParameters params = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();
            
            jobLauncher.run(csvShopTagJob, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return "form/main";
    }

    public Map<String, ShopTag> getShopTagsMap() {
        return shopTagRepository.findAll().stream()
            .collect(Collectors.toMap(ShopTag::getName, Function.identity(), (existing, replacement) -> existing));
    }
}