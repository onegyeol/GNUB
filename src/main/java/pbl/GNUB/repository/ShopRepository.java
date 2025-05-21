package pbl.GNUB.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pbl.GNUB.entity.Shop;

public interface ShopRepository extends JpaRepository<Shop, Long> {

        List<Shop> findTop28ByOrderByLikeCountDesc(); // 좋아요 수 기준으로 상위 28개 조회

        Shop getShopById(Long id); // 해당 id로 음식점 조회

        @Query("SELECT s FROM Shop s WHERE s.lat BETWEEN :swLat AND :neLat AND s.lng BETWEEN :swLng AND :neLng")
        List<Shop> findShopsByBounds(@Param("swLat") Double swLat, @Param("neLat") Double neLat,
                        @Param("swLng") Double swLng, @Param("neLng") Double neLng);

        /*
         * 이전 태그로 사용했던 쿼리문들
         * 
         * @Query("SELECT DISTINCT s FROM Shop s " +
         * "JOIN FETCH s.shopTags st " +
         * "WHERE st.hygiene = 1 OR st.revisit = 1 OR st.recent = 1 OR st.delicious = 1 OR "
         * +
         * "st.goodValue = 1 OR st.mood = 1 OR st.fresh = 1 OR st.kindness = 1 OR st.alone = 1"
         * )
         * List<Shop> findAllWithActiveTags();
         */

        @Query("SELECT DISTINCT s FROM Shop s " +
                        "LEFT JOIN s.shopMenus m " +
                        "WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
                        "LOWER(m.menuName) LIKE LOWER(CONCAT('%', :query, '%'))")
        List<Shop> searchShops(@Param("query") String query);

        @Query("SELECT s FROM Shop s JOIN FETCH s.shopTags")
        @EntityGraph(attributePaths = "shopTags")
        List<Shop> findAllWithTags();

        @Query("SELECT DISTINCT s FROM Shop s JOIN s.shopTags t WHERE " +
                        "(:tagName = '혼밥하기 좋아요' AND t.alone > 0) OR " +
                        "(:tagName = '데이트하기 좋아요' AND t.date > 0) OR " +
                        "(:tagName = '맛있어요' AND t.delicious > 0) OR " +
                        "(:tagName = '신선해요' AND t.fresh > 0) OR " +
                        "(:tagName = '가성비 좋아요' AND t.goodValue > 0) OR " +
                        "(:tagName = '청결해요' AND t.hygiene > 0) OR " +
                        "(:tagName = '친절해요' AND t.kindness > 0) OR " +
                        "(:tagName = '단체로 가기 좋아요' AND t.many > 0) OR " +
                        "(:tagName = '분위기 좋아요' AND t.mood > 0) OR " +
                        "(:tagName = '주차가 가능해요' AND t.parking > 0) OR " +
                        "(:tagName = '아쉬워요' AND t.recent > 0)")
        List<Shop> findShopsByTagName(@Param("tagName") String tagName);

        @Query("SELECT DISTINCT s FROM Shop s LEFT JOIN FETCH s.shopMenus")
        List<Shop> findAllWithMenus();

        @Query("SELECT DISTINCT s FROM Shop s " +
                        "LEFT JOIN FETCH s.shopMenus m " +
                        "WHERE (:tagName = '혼밥하기 좋아요' AND EXISTS (SELECT 1 FROM s.shopTags t WHERE t.alone > 0)) OR " +
                        "(:tagName = '데이트하기 좋아요' AND EXISTS (SELECT 1 FROM s.shopTags t WHERE t.date > 0)) OR " +
                        "(:tagName = '맛있어요' AND EXISTS (SELECT 1 FROM s.shopTags t WHERE t.delicious > 0)) OR " +
                        "(:tagName = '신선해요' AND EXISTS (SELECT 1 FROM s.shopTags t WHERE t.fresh > 0)) OR " +
                        "(:tagName = '가성비 좋아요' AND EXISTS (SELECT 1 FROM s.shopTags t WHERE t.goodValue > 0)) OR " +
                        "(:tagName = '청결해요' AND EXISTS (SELECT 1 FROM s.shopTags t WHERE t.hygiene > 0)) OR " +
                        "(:tagName = '친절해요' AND EXISTS (SELECT 1 FROM s.shopTags t WHERE t.kindness > 0)) OR " +
                        "(:tagName = '단체로 가기 좋아요' AND EXISTS (SELECT 1 FROM s.shopTags t WHERE t.many > 0)) OR " +
                        "(:tagName = '분위기 좋아요' AND EXISTS (SELECT 1 FROM s.shopTags t WHERE t.mood > 0)) OR " +
                        "(:tagName = '주차가 가능해요' AND EXISTS (SELECT 1 FROM s.shopTags t WHERE t.parking > 0)) OR " +
                        "(:tagName = '아쉬워요' AND EXISTS (SELECT 1 FROM s.shopTags t WHERE t.recent > 0))")
        List<Shop> findShopsByTagNameWithMenus(@Param("tagName") String tagName);

        @Query("SELECT s.id, t FROM Shop s JOIN s.shopTags t WHERE s.id IN :ids")
        List<Object[]> findShopTagsByShopIds(@Param("ids") List<Long> ids);

        /*
         * @Query("SELECT DISTINCT s FROM Shop s " +
         * "JOIN s.shopTags t " +
         * "LEFT JOIN s.shopMenus m " +
         * "WHERE (" +
         * "(:tag = 'hygiene' AND t.hygiene = 1) OR " +
         * "(:tag = 'revisit' AND t.revisit = 1) OR " +
         * "(:tag = 'recent' AND t.recent = 1) OR " +
         * "(:tag = 'delicious' AND t.delicious = 1) OR " +
         * "(:tag = 'good_value' AND t.goodValue = 1) OR " +
         * "(:tag = 'mood' AND t.mood = 1) OR " +
         * "(:tag = 'fresh' AND t.fresh = 1) OR " +
         * "(:tag = 'kindness' AND t.kindness = 1) OR " +
         * "(:tag = 'alone' AND t.alone = 1) OR " +
         * "(:tag = 'chilam_dong' AND t.chilamDong = 1) OR " +
         * "(:tag = 'gajwa_dong' AND t.gajwaDong = 1)" +
         * ") " +
         * "AND (:query IS NULL OR :query = '' OR " +
         * "(LOWER(s.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
         * "LOWER(m.menuName) LIKE LOWER(CONCAT('%', :query, '%'))))")
         * List<Shop> findShopsByDynamicTag(@Param("tag") String tag, @Param("query")
         * String query);
         */
}
