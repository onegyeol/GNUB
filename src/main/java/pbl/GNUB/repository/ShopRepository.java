package pbl.GNUB.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pbl.GNUB.entity.Shop;

public interface ShopRepository extends JpaRepository<Shop, Long>{
   // id가 1부터 30까지인 데이터를 조회
   List<Shop> findByIdBetween(Long startId, Long endId);
   //List<Shop> findTop30ByOrderByLikeCountDesc();  // 좋아요 수 기준으로 30개 가져오기
   Shop getShopById(Long id); // 해당 i로 음식점 조회

   @Query("SELECT s FROM Shop s WHERE "
         + "LOWER(s.name) LIKE LOWER(CONCAT('%', :query, '%')) OR "
         + "LOWER(s.mainMenu) LIKE LOWER(CONCAT('%', :query, '%'))")
   List<Shop> searchShops(@Param("query") String query);
}
