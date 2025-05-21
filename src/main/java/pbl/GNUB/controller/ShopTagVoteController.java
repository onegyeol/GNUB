package pbl.GNUB.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody; // 추가
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;

import jakarta.servlet.http.HttpServletRequest;
import pbl.GNUB.repository.ShopTagVoteRepository;
import pbl.GNUB.repository.ShopRepository;
import pbl.GNUB.repository.ShopTagRepository;
import pbl.GNUB.entity.Shop;
import pbl.GNUB.entity.ShopTag;
import pbl.GNUB.entity.ShopTagVote;
import pbl.GNUB.entity.Member;
import pbl.GNUB.repository.MemberRepository;

@Controller
public class ShopTagVoteController {

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private ShopTagVoteRepository shopTagVoteRepository;

    @Autowired
    private ShopTagRepository shopTagRepository;

    @Autowired
    private MemberRepository memberRepository;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/voteTag")
    @ResponseBody // 꼭 붙여야 브라우저가 JSON으로 인식함
    public ResponseEntity<Integer> voteTag(@RequestParam("shopId") Long shopId,
            @RequestParam("tagName") String tagName,
            Principal principal) {
        Member member = memberRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("No member"));
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new RuntimeException("No shop"));

        boolean voted = shopTagVoteRepository.existsByMemberAndShopAndTagName(member, shop, tagName);
        if (voted) {
            shopTagVoteRepository.deleteByMemberAndShopAndTagName(member, shop, tagName);
        } else {
            ShopTagVote vote = new ShopTagVote();
            vote.setMember(member);
            vote.setShop(shop);
            vote.setTagName(tagName);
            shopTagVoteRepository.save(vote);
        }

        long voteCount = shopTagVoteRepository.countByShopAndTagName(shop, tagName);

        ShopTag tag = shop.getShopTags().stream()
                .filter(t -> t.getRestId().equals(shop.getRestId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("ShopTag not found"));

        int base = switch (tagName) {
            case "맛있어요" -> tag.getDelicious();
            case "청결해요" -> tag.getHygiene();
            case "친절해요" -> tag.getKindness();
            case "신선해요" -> tag.getFresh();
            case "혼밥하기 좋아요" -> tag.getAlone();
            case "데이트하기 좋아요" -> tag.getDate();
            case "가성비 좋아요" -> tag.getGoodValue();
            case "단체로 가기 좋아요" -> tag.getMany();
            case "분위기 좋아요" -> tag.getMood();
            case "주차가 가능해요" -> tag.getParking();
            case "아쉬워요" -> tag.getRecent();
            default -> throw new IllegalArgumentException("Invalid tag name");
        };

        int total = base + (int) voteCount;

        return ResponseEntity.ok(total); // ✅ 숫자만 JSON으로 응답
    }
}