package pbl.GNUB.service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pbl.GNUB.dto.ShopTagDto;
import pbl.GNUB.entity.Shop;
import pbl.GNUB.entity.ShopTag;
import pbl.GNUB.repository.ShopRepository;
import pbl.GNUB.repository.ShopTagRepository;

@Service
public class TagService {
    private final ShopRepository shopRepository;

    private static final Map<String, Function<ShopTag, Integer>> TAG_SCORE_GETTERS = new HashMap<>();
    static {
        TAG_SCORE_GETTERS.put("혼밥하기 좋아요", tag -> tag.getAlone());
        TAG_SCORE_GETTERS.put("데이트하기 좋아요", tag -> tag.getDate());
        TAG_SCORE_GETTERS.put("맛있어요", tag -> tag.getDelicious());
        TAG_SCORE_GETTERS.put("신선해요", tag -> tag.getFresh());
        TAG_SCORE_GETTERS.put("가성비 좋아요", tag -> tag.getGoodValue());
        TAG_SCORE_GETTERS.put("청결해요", tag -> tag.getHygiene());
        TAG_SCORE_GETTERS.put("친절해요", tag -> tag.getKindness());
        TAG_SCORE_GETTERS.put("단체로 가기 좋아요", tag -> tag.getMany());
        TAG_SCORE_GETTERS.put("분위기 좋아요", tag -> tag.getMood());
        TAG_SCORE_GETTERS.put("주차가 가능해요", tag -> tag.getParking());
        TAG_SCORE_GETTERS.put("아쉬워요", tag -> tag.getRecent());
    }

    @Autowired
    public TagService(ShopRepository shopRepository) {
        this.shopRepository = shopRepository;
    }

    public Map<String, List<Shop>> getAllTaggedShopsTop100() {
        List<Shop> shops = shopRepository.findAllWithTags();
        Map<String, Integer> tagTotals = new HashMap<>();
        Map<String, Set<Shop>> tagToShops = new HashMap<>();

        for (Shop shop : shops) {
            for (ShopTag tag : shop.getShopTags()) {
                for (Map.Entry<String, Function<ShopTag, Integer>> entry : TAG_SCORE_GETTERS.entrySet()) {
                    String tagName = entry.getKey();
                    int score = entry.getValue().apply(tag);
                    if (score > 0) {
                        tagTotals.merge(tagName, score, Integer::sum);
                        tagToShops.computeIfAbsent(tagName, k -> new HashSet<>()).add(shop);
                    }
                }
            }
        }

        Map<String, List<Shop>> result = new LinkedHashMap<>();
        for (Map.Entry<String, Set<Shop>> entry : tagToShops.entrySet()) {
            String tagName = entry.getKey();
            Function<ShopTag, Integer> scoreFunc = TAG_SCORE_GETTERS.get(tagName);

            List<Shop> sorted = entry.getValue().stream()
                    .sorted((s1, s2) -> {
                        ShopTag t1 = s1.getShopTags().get(0); // 단일 ShopTag 기준
                        ShopTag t2 = s2.getShopTags().get(0);
                        return Integer.compare(scoreFunc.apply(t2), scoreFunc.apply(t1));
                    })
                    .limit(100)
                    .collect(Collectors.toList());

            result.put(tagName, sorted);
        }

        // ✅ 태그 총합 기준으로 정렬된 Map 반환
        return tagTotals.entrySet().stream()
                .sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> result.getOrDefault(e.getKey(), List.of()),
                        (e1, e2) -> e1,
                        LinkedHashMap::new));

    }
}
