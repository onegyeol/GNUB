package pbl.GNUB.controller;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import pbl.GNUB.service.TagService;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;
import pbl.GNUB.entity.Shop;
import pbl.GNUB.entity.ShopTag;
import pbl.GNUB.repository.ShopRepository;
import pbl.GNUB.repository.ShopTagRepository;

@Slf4j
@Controller
public class TagController {
    private final JobLauncher jobLauncher;
    private final Job csvShopTagJob;
    private final ShopTagRepository shopTagRepository;
    private final ShopRepository shopRepository;
    private final TagService tagService;

    @Autowired
    public TagController(
            JobLauncher jobLauncher,
            Job csvShopTagJob,
            ShopTagRepository shopTagRepository,
            ShopRepository shopRepository,
            TagService tagService // ✅ 주입
    ) {
        this.jobLauncher = jobLauncher;
        this.csvShopTagJob = csvShopTagJob;
        this.shopTagRepository = shopTagRepository;
        this.shopRepository = shopRepository;
        this.tagService = tagService; // ✅ 필드 초기화
    }

    public record TagGroup(String hashId, List<Shop> shops) {
    }

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

        return "redirect:/main";
    }
}
