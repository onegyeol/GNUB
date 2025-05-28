package pbl.GNUB.controller.api;

import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pbl.GNUB.dto.ShopDto;
import pbl.GNUB.entity.Member;
import pbl.GNUB.entity.Shop;
import pbl.GNUB.entity.ShopTagVote;
import pbl.GNUB.repository.MemberRepository;
import pbl.GNUB.repository.ShopTagVoteRepository;
import pbl.GNUB.service.ShopService;
import pbl.GNUB.service.ShopTagMappingService;
import pbl.GNUB.service.TagService;

@RestController
@RequestMapping("/api/main")
public class MainApiController {

    private final ShopService shopService;
    private final TagService tagService;
    private final ShopTagMappingService mappingService;
    private final MemberRepository memberRepository;
    private final ShopTagVoteRepository shopTagVoteRepository;

    public MainApiController(ShopService shopService, TagService tagService,
                             ShopTagMappingService mappingService,
                             MemberRepository memberRepository,
                             ShopTagVoteRepository shopTagVoteRepository) {
        this.shopService = shopService;
        this.tagService = tagService;
        this.mappingService = mappingService;
        this.memberRepository = memberRepository;
        this.shopTagVoteRepository = shopTagVoteRepository;
    }

    @GetMapping
    public Map<String, Object> getMainPageData(Principal principal) {
        mappingService.mapShopAndShopTagsByName(); // Shop-Tag 연관 매핑

        Map<String, Object> response = new HashMap<>();

        // ✅ 1. 카테고리 분류
        List<Shop> shops = shopService.getAllShops();
        Map<String, List<ShopDto>> categorizedShops = new HashMap<>();
        for (Shop shop : shops) {
            String category = (shop.getCategory() == null || shop.getCategory().isEmpty()) ? "기타" : shop.getCategory();
            categorizedShops
                .computeIfAbsent(category, k -> new ArrayList<>())
                .add(new ShopDto(shop));
        }
        response.put("categorizedShops", categorizedShops);

        // ✅ 2. 태그 기반 가게 목록 및 해시 ID
        Map<String, List<Shop>> tagMap = tagService.getAllTaggedShopsTop100();
        Map<String, Object> taggedShops = new LinkedHashMap<>();
        Set<Long> uniqueShopIds = new HashSet<>();

        for (Map.Entry<String, List<Shop>> entry : tagMap.entrySet()) {
            String tagName = entry.getKey();
            String hashId = DigestUtils.md5DigestAsHex(tagName.getBytes(StandardCharsets.UTF_8));

            List<ShopDto> dtoList = entry.getValue().stream()
                .peek(shop -> uniqueShopIds.add(shop.getId()))
                .map(ShopDto::new)
                .collect(Collectors.toList());

            Map<String, Object> tagInfo = new HashMap<>();
            tagInfo.put("hashId", hashId);
            tagInfo.put("shops", dtoList);

            taggedShops.put(tagName, tagInfo);
        }

        response.put("taggedShops", taggedShops);
        response.put("uniqueShopCount", uniqueShopIds.size());

        // ✅ 3. 사용자 투표 여부 (votedMap)
        if (principal != null) {
            String email = principal.getName();
            Member member = memberRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("로그인 사용자 정보를 찾을 수 없음: " + email));

            List<ShopTagVote> allVotes = shopTagVoteRepository.findByMember(member);
            Map<Long, Map<String, Boolean>> votedMap = new HashMap<>();

            for (ShopTagVote vote : allVotes) {
                votedMap
                    .computeIfAbsent(vote.getShop().getId(), k -> new HashMap<>())
                    .put(vote.getTagName(), true);
            }

            response.put("votedMap", votedMap);
        }

        return response;
    }
}
