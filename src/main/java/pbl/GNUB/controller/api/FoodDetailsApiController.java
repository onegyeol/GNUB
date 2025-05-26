package pbl.GNUB.controller.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import pbl.GNUB.entity.*;
import pbl.GNUB.repository.MemberRepository;
import pbl.GNUB.repository.ShopTagVoteRepository;
import pbl.GNUB.service.BookmarkService;
import pbl.GNUB.service.ShopService;

import java.security.Principal;
import java.util.*;

@RestController
@RequestMapping("/api/foodDetails")
@RequiredArgsConstructor
public class FoodDetailsApiController {

    private final ShopService shopService;
    private final BookmarkService bookmarkService;
    private final MemberRepository memberRepository;
    private final ShopTagVoteRepository shopTagVoteRepository;

    @GetMapping("/{id}")
    public ResponseEntity<?> getFoodDetails(@PathVariable Long id, Principal principal) {
        Shop shop = shopService.findShopById(id);
        if (shop == null) return ResponseEntity.notFound().build();

        List<ShopMenu> shopMenus = shopService.getMenusByShopName(shop.getName());

        boolean isBookmarked = false;
        boolean isLoggedIn = false;
        Long memberId = null;
        String memberName = null;

        if (principal != null) {
            Member member = memberRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("사용자 정보 없음"));
            isBookmarked = bookmarkService.isBookmarked(member, shop);
            isLoggedIn = true;
            memberId = member.getId();
            memberName = member.getName();
        }

        // 태그 점수 + 투표 수 계산
        List<String> allTagNames = Arrays.asList(
            "맛있어요", "청결해요", "친절해요", "신선해요", "혼밥하기 좋아요",
            "데이트하기 좋아요", "가성비 좋아요", "단체로 가기 좋아요",
            "분위기 좋아요", "주차가 가능해요", "아쉬워요"
        );

        Map<String, Integer> tagCounts = new LinkedHashMap<>();
        for (String tagName : allTagNames) {
            tagCounts.put(tagName, 0);
        }

        ShopTag tag = shop.getShopTags().stream().findFirst().orElse(null);
        if (tag != null) {
            Map<String, Integer> baseScores = new HashMap<>();

            baseScores.put("맛있어요", tag.getDelicious());
            baseScores.put("청결해요", tag.getHygiene());
            baseScores.put("친절해요", tag.getKindness());
            baseScores.put("신선해요", tag.getFresh());
            baseScores.put("혼밥하기 좋아요", tag.getAlone());
            baseScores.put("데이트하기 좋아요", tag.getDate());
            baseScores.put("가성비 좋아요", tag.getGoodValue());
            baseScores.put("단체로 가기 좋아요", tag.getMany());
            baseScores.put("분위기 좋아요", tag.getMood());
            baseScores.put("주차가 가능해요", tag.getParking());
            baseScores.put("아쉬워요", tag.getRecent());

            for (String tagName : allTagNames) {
                int base = baseScores.getOrDefault(tagName, 0);
                int votes = (int) shopTagVoteRepository.countByShopAndTagName(shop, tagName);
                tagCounts.put(tagName, base + votes);
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("shop", shop);
        response.put("shopMenus", shopMenus);
        response.put("isBookmarked", isBookmarked);
        response.put("isLoggedIn", isLoggedIn);
        response.put("memberId", memberId);
        response.put("memberName", memberName);
        response.put("tagCounts", tagCounts);

        return ResponseEntity.ok(response);
    }
}
