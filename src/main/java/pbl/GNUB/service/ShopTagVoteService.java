package pbl.GNUB.service;

import jakarta.transaction.Transactional;
import java.util.*;

import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pbl.GNUB.entity.Member;
import pbl.GNUB.entity.Shop;
import pbl.GNUB.entity.ShopTag;
import pbl.GNUB.entity.ShopTagVote;
import pbl.GNUB.repository.ShopTagVoteRepository;

@Service
public class ShopTagVoteService {

    @Autowired
    private ShopTagVoteRepository voteRepository;

    // ✅ 유효한 태그 목록 (다른 클래스에서도 접근 가능)
    public static final List<String> VALID_TAGS = List.of(
            "맛있어요", "청결해요", "친절해요", "신선해요",
            "혼밥하기 좋아요", "데이트하기 좋아요", "가성비 좋아요",
            "단체로 가기 좋아요", "분위기 좋아요", "주차가 가능해요", "아쉬워요");

    // ✅ 태그별 getter 함수 매핑 (Map 형태, 외부에서도 접근 가능)
    public static final Map<String, Function<ShopTag, Integer>> TAG_SCORE_GETTERS = Map.ofEntries(
            Map.entry("맛있어요", ShopTag::getDelicious),
            Map.entry("청결해요", ShopTag::getHygiene),
            Map.entry("친절해요", ShopTag::getKindness),
            Map.entry("신선해요", ShopTag::getFresh),
            Map.entry("혼밥하기 좋아요", ShopTag::getAlone),
            Map.entry("데이트하기 좋아요", ShopTag::getDate),
            Map.entry("가성비 좋아요", ShopTag::getGoodValue),
            Map.entry("단체로 가기 좋아요", ShopTag::getMany),
            Map.entry("분위기 좋아요", ShopTag::getMood),
            Map.entry("주차가 가능해요", ShopTag::getParking),
            Map.entry("아쉬워요", ShopTag::getRecent));

    // ✅ 태그 투표 토글 및 점수 계산
    @Transactional
    public int toggleVote(Member member, Shop shop, String tagName, ShopTag tag) {
        if (!VALID_TAGS.contains(tagName)) {
            throw new IllegalArgumentException("Invalid tag name: " + tagName);
        }

        boolean voted = voteRepository.existsByMemberAndShopAndTagName(member, shop, tagName);
        if (voted) {
            voteRepository.deleteByMemberAndShopAndTagName(member, shop, tagName);
        } else {
            ShopTagVote vote = new ShopTagVote();
            vote.setMember(member);
            vote.setShop(shop);
            vote.setTagName(tagName);
            voteRepository.save(vote);
        }

        int base = TAG_SCORE_GETTERS.get(tagName).apply(tag);
        long voteCount = voteRepository.countByShopAndTagName(shop, tagName);
        return base + (int) voteCount;
    }

    // ✅ 레이더 차트용 태그 점수 계산
    public Map<String, Integer> getRadarChartData(Shop shop) {
        ShopTag tag = shop.getShopTags().stream()
                .filter(t -> t.getRestId().equals(shop.getRestId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No tag found for shop"));

        Map<String, Integer> result = new LinkedHashMap<>();

        for (String tagName : VALID_TAGS) {
            int base = 0;
            try {
                base = TAG_SCORE_GETTERS.get(tagName).apply(tag);
            } catch (Exception e) {
                base = 0;
            }
            long voteCount = voteRepository.countByShopAndTagName(shop, tagName);
            result.put(tagName, base + (int) voteCount);
        }

        return result;
    }
}