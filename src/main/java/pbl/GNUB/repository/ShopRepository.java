package pbl.GNUB.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pbl.GNUB.entity.Shop;

public interface ShopRepository extends JpaRepository<Shop, Long>{
      List<Shop> findTop28ByOrderByLikeCountDesc(); // 좋아요 수 기준으로 상위 28개 조회

      Shop getShopById(Long id); // 해당 i로 음식점 조회

      @Query("SELECT s FROM Shop s WHERE "
            + "LOWER(s.name) LIKE LOWER(CONCAT('%', :query, '%')) OR "
            + "LOWER(s.mainMenu) LIKE LOWER(CONCAT('%', :query, '%'))")
      List<Shop> searchShops(@Param("query") String query);

      @Query("SELECT DISTINCT s FROM Shop s " +
            "JOIN s.shopTags t " +
            "WHERE (" +
            "(:tag = 'hygiene' AND t.hygiene = 1) OR " +
            "(:tag = 'revisit' AND t.revisit = 1) OR " +
            "(:tag = 'recent' AND t.recent = 1) OR " +
            "(:tag = 'delicious' AND t.delicious = 1) OR " +
            "(:tag = 'good_value' AND t.goodValue = 1) OR " +
            "(:tag = 'mood' AND t.mood = 1) OR " +
            "(:tag = 'fresh' AND t.fresh = 1) OR " +
            "(:tag = 'kindness' AND t.kindness = 1) OR " +
            "(:tag = 'alone' AND t.alone = 1) OR " +
            "(:tag = 'chilam_dong' AND t.chilamDong = 1) OR " +
            "(:tag = 'gajwa_dong' AND t.gajwaDong = 1)" +
            ") " +
            "AND (:query IS NULL OR :query = '' OR " +
            "(LOWER(s.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(s.mainMenu) LIKE LOWER(CONCAT('%', :query, '%'))))")
      List<Shop> findShopsByDynamicTag(@Param("tag") String tag, @Param("query") String query);


}
