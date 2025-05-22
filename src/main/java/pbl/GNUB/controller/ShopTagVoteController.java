package pbl.GNUB.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import pbl.GNUB.entity.Member;
import pbl.GNUB.entity.Shop;
import pbl.GNUB.entity.ShopTag;
import pbl.GNUB.repository.MemberRepository;
import pbl.GNUB.repository.ShopRepository;
import pbl.GNUB.service.ShopTagVoteService;

@Controller
public class ShopTagVoteController {

    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ShopTagVoteService voteService;

    @PostMapping("/voteTag")
    @PreAuthorize("hasRole('USER')")
    @ResponseBody
    public ResponseEntity<Integer> voteTag(@RequestParam("shopId") Long shopId,
            @RequestParam("tagName") String tagName,
            Principal principal) {

        Member member = memberRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("No member"));
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new RuntimeException("No shop"));

        ShopTag tag = shop.getShopTags().stream()
                .filter(t -> t.getRestId().equals(shop.getRestId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No tag"));

        int totalScore = voteService.toggleVote(member, shop, tagName, tag);
        return ResponseEntity.ok(totalScore);
    }
}
