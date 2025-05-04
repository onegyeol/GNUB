package pbl.GNUB.controller;

import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import lombok.extern.slf4j.Slf4j;
import pbl.GNUB.entity.Member;
import pbl.GNUB.entity.Shop;
import pbl.GNUB.entity.ShopMenu;
import pbl.GNUB.repository.MemberRepository;
import pbl.GNUB.service.BookmarkService;
import pbl.GNUB.service.ShopService;
import pbl.GNUB.service.ShopTagMappingService;
import pbl.GNUB.service.TagService;

@Slf4j
@Controller
public class MainController {

    @Value("${google.maps.api.key}")
    private String googleMapsApiKey;

    private final JobLauncher jobLauncher;
    private final Job csvShopJob;
    private final ShopService shopService;
    private final TagController tagController;
    private final ShopTagMappingService mappingService;
    private final MemberRepository memberRepository;
    private final BookmarkService bookmarkService;
    private final TagService tagService;

    @Autowired
    public MainController(JobLauncher jobLauncher, Job csvShopJob,
            JobRepository jobRepository, ShopService shopService,
            TagController tagController, ShopTagMappingService mappingService,
            MemberRepository memberRepository, BookmarkService bookmarkService, TagService tagService) {
        this.jobLauncher = jobLauncher;
        this.csvShopJob = csvShopJob;
        this.shopService = shopService;
        this.tagController = tagController;
        this.mappingService = mappingService;
        this.memberRepository = memberRepository;
        this.bookmarkService = bookmarkService;
        this.tagService = tagService;
    }

    @GetMapping("/main")
    public String showMainPage(Model model, Principal principal) {

        if (principal != null) {
            System.out.println("✅ 로그인된 사용자: " + principal.getName());
        } else {
            System.out.println("❌ 비로그인 상태");
        }

        // 카테고리 분류
        List<Shop> shops = shopService.getAllShops();
        Map<String, List<Shop>> categorizedShops = new HashMap<>();
        for (Shop shop : shops) {
            String category = (shop.getCategory() == null || shop.getCategory().isEmpty()) ? "기타" : shop.getCategory();
            categorizedShops.computeIfAbsent(category, k -> new ArrayList<>()).add(shop);
        }
        model.addAttribute("categorizedShopsList", categorizedShops.entrySet());

        // 태그 분류
        Map<String, List<Shop>> tagMap = tagService.getAllTaggedShopsTop100();
        Map<String, Map<String, Object>> taggedShops = new LinkedHashMap<>();

        for (Map.Entry<String, List<Shop>> entry : tagMap.entrySet()) {
            String tagName = entry.getKey();
            String hashId = DigestUtils.md5DigestAsHex(tagName.getBytes(StandardCharsets.UTF_8));
            Map<String, Object> tagInfo = new HashMap<>();
            tagInfo.put("hashId", hashId);
            tagInfo.put("shops", entry.getValue());
            taggedShops.put(tagName, tagInfo);
        }

        // 중복 제거한 unique shop 수 계산
        Set<Long> uniqueShopIds = new HashSet<>();
        for (Map<String, Object> tagInfo : taggedShops.values()) {
            @SuppressWarnings("unchecked")
            List<Shop> shopList = (List<Shop>) tagInfo.get("shops");
            for (Shop shop : shopList) {
                uniqueShopIds.add(shop.getId());
            }
        }

        model.addAttribute("uniqueShopCount", uniqueShopIds.size());
        model.addAttribute("taggedShops", taggedShops);

        return "form/main";
    }

    /*
     * ShopTag.csv 파일 완성 시 까지 주석 처리
     * 
     * // 1. 먼저 매핑을 수행 (이름 기준)
     * // mappingService.mapShopAndShopTagsByName();
     * 
     * // 2. 매핑된 데이터를 기반으로 태그 맵 가져오기
     * Map<String, List<String>> shopTagsMap = tagController.getShopTagsMap();
     * mappingService.mapShopAndShopTagsById(); // shop과 shopTag id 매핑
     * 
     * // 3. 좋아요 기준 상위 28개 Shop 가져오기
     * List<Shop> shops = shopService.getTop28ShopsByLikes();
     * 
     * 
     * HashMap<String, List<Shop>> taggedShops = new HashMap<>();
     * 
     * for (Shop shop : shops) {
     * List<String> shopTags = shopTagsMap.get(shop.getName());
     * if (shopTags != null) {
     * for (String tag : shopTags) {
     * taggedShops.computeIfAbsent(tag, k -> new ArrayList<>()).add(shop);
     * }
     * }
     * }
     * 
     * model.addAttribute("shops", shops);
     * model.addAttribute("shopTagsMap", shopTagsMap); // 이 부분도 추가함
     * model.addAttribute("taggedShops", taggedShops);
     * 
     */

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
    public String foodDetailsPage(@PathVariable("id") Long id, Model model, Principal principal) {
        Shop shop = shopService.findShopById(id);

        if (shop == null) {
            return "error";
        }

        List<ShopMenu> shopMenus = shopService.getMenusByShopName(shop.getName());

        model.addAttribute("shop", shop);
        model.addAttribute("shopMenus", shopMenus);
        model.addAttribute("googleMapsApiKey", googleMapsApiKey);

        boolean isBookmarked = false;

        // 로그인 여부 확인
        if (principal != null) {
            String email = principal.getName(); // 로그인된 사용자 email
            Member member = memberRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("로그인 사용자 정보를 찾을 수 없음: " + email));
            isBookmarked = bookmarkService.isBookmarked(member, shop);

            model.addAttribute("isLoggedIn", true);
            model.addAttribute("memberId", member.getId());
            model.addAttribute("memberName", member.getName());
        } else {
            model.addAttribute("isLoggedIn", false);
        }

        model.addAttribute("isBookmarked", isBookmarked);

        return "form/foodDetails";
    }

    // GPT 화면
    @GetMapping("/ask")
    public String GptPage() {
        return "form/recommend"; // 로그인 폼 뷰를 반환
    }
}
