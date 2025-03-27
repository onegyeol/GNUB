package pbl.GNUB.controller;

import java.util.ArrayList;
import java.util.HashMap;
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
import pbl.GNUB.entity.Shop;
import pbl.GNUB.entity.ShopTag;
import pbl.GNUB.repository.ShopRepository;
import pbl.GNUB.repository.ShopTagRepository;

@Slf4j
@Controller
public class TagController{
    private final JobLauncher jobLauncher;
    private final Job csvShopTagJob;
    private final ShopTagRepository shopTagRepository;
    private final ShopRepository shopRepository;
    ;

    @Autowired
    public TagController(JobLauncher jobLauncher, Job csvShopTagJob, ShopTagRepository shopTagRepository, ShopRepository shopRepository) {
        this.jobLauncher =jobLauncher;
        this.csvShopTagJob =csvShopTagJob;
        this.shopTagRepository =shopTagRepository;
        this.shopRepository = shopRepository;
    }

    @GetMapping("/importTags")
    public String importTagsFromCsv() {
        try{
            JobParameters params =new JobParametersBuilder()
                                    .addLong("time", System.currentTimeMillis()).toJobParameters();
            jobLauncher.run(csvShopTagJob, params);
        } catch(Exception e) {
            e.printStackTrace();
        }

        return "form/main";
    }

    /* 
    기존에 1로 매핑했지만 현재 1이 아닌 정수들이 들어있어서 못쓰지만 일단 주석처리함 (삭제안하고)
    
    public Map<String, List<String>> getShopTagsMap() {
        List<Shop> shops = shopRepository.findAllWithActiveTags();
        Map<String, List<String>> shopTagsMap = new HashMap<>();
    
        for (Shop shop : shops) {
            List<String> activeTags = new ArrayList<>();
    
            for (ShopTag tag : shop.getShopTags()) {
                System.out.println("🔵 Tag for Shop '" + shop.getName() + "': " + tag.getName());
    
                if (tag.getHygiene() == 1) activeTags.add("위생등급제 가게");
                if (tag.getRevisit() == 1) activeTags.add("재방문률이 높은");
                if (tag.getRecent() == 1) activeTags.add("최근에 자주가는");
                if (tag.getDelicious() == 1) activeTags.add("맛있는");
                if (tag.getGoodValue() == 1) activeTags.add("가성비");
                if (tag.getMood() == 1) activeTags.add("깔끔하고 분위기가 좋은");
                if (tag.getFresh() == 1) activeTags.add("신선한");
                if (tag.getKindness() == 1) activeTags.add("친절한");
                if (tag.getAlone() == 1) activeTags.add("혼밥");
            }
    
            shopTagsMap.put(shop.getName(), activeTags);
            System.out.println("✅ Shop: " + shop.getName() + " | Tags: " + activeTags);
        }
    
        System.out.println("🎯 최종 shopTagsMap 결과: " + shopTagsMap);
    
        return shopTagsMap;
    }*/
    
    
    
}
