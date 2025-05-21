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
import pbl.GNUB.repository.ShopTagVoteRepository;

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

    public List<Shop> getTop100ShopsByTag(String tagName) {
        Function<ShopTag, Integer> scoreFunc = TAG_SCORE_GETTERS.get(tagName);
        if (scoreFunc == null)
            return List.of();

        List<Shop> shops = shopRepository.findShopsByTagNameWithMenus(tagName);
        List<Long> shopIds = shops.stream().map(Shop::getId).toList();

        List<Object[]> rawTags = shopRepository.findShopTagsByShopIds(shopIds);
        Map<Long, List<ShopTag>> tagMap = rawTags.stream()
                .collect(Collectors.groupingBy(
                        row -> (Long) row[0],
                        Collectors.mapping(row -> (ShopTag) row[1], Collectors.toList())));

        for (Shop shop : shops) {
            shop.setShopTags(tagMap.getOrDefault(shop.getId(), List.of()));
        }

        return shops.stream()
                .filter(shop -> shop.getShopTags().stream().anyMatch(tag -> scoreFunc.apply(tag) > 0))
                .sorted((s1, s2) -> {
                    int score1 = s1.getShopTags().stream().map(scoreFunc).findFirst().orElse(0);
                    int score2 = s2.getShopTags().stream().map(scoreFunc).findFirst().orElse(0);
                    return Integer.compare(score2, score1); // 내림차순
                })
                .limit(100)
                .collect(Collectors.toList());
    }

    @Autowired
    private ShopTagVoteRepository shopTagVoteRepository;

    public Map<String, Integer> getRadarChartData(Shop shop) {
        ShopTag tag = shop.getShopTags().stream()
                .filter(t -> t.getRestId().equals(shop.getRestId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No tag"));

        Map<String, Integer> result = new LinkedHashMap<>();
        result.put("맛있어요", tag.getDelicious() + (int) shopTagVoteRepository.countByShopAndTagName(shop, "맛있어요"));
        result.put("신선해요", tag.getFresh() + (int) shopTagVoteRepository.countByShopAndTagName(shop, "신선해요"));
        result.put("가성비 좋아요", tag.getGoodValue() + (int) shopTagVoteRepository.countByShopAndTagName(shop, "가성비 좋아요"));
        result.put("청결해요", tag.getHygiene() + (int) shopTagVoteRepository.countByShopAndTagName(shop, "청결해요"));
        result.put("친절해요", tag.getKindness() + (int) shopTagVoteRepository.countByShopAndTagName(shop, "친절해요"));
        result.put("분위기 좋아요", tag.getMood() + (int) shopTagVoteRepository.countByShopAndTagName(shop, "분위기 좋아요"));
        result.put("혼밥하기 좋아요", tag.getAlone() + (int) shopTagVoteRepository.countByShopAndTagName(shop, "혼밥하기 좋아요"));
        result.put("데이트하기 좋아요", tag.getDate() + (int) shopTagVoteRepository.countByShopAndTagName(shop, "데이트하기 좋아요"));
        result.put("단체로 가기 좋아요", tag.getMany() + (int) shopTagVoteRepository.countByShopAndTagName(shop, "단체로 가기 좋아요"));
        result.put("주차가 가능해요", tag.getParking() + (int) shopTagVoteRepository.countByShopAndTagName(shop, "주차가 가능해요"));
        result.put("아쉬워요", tag.getRecent() + (int) shopTagVoteRepository.countByShopAndTagName(shop, "아쉬워요"));
        return result;
    }

}
