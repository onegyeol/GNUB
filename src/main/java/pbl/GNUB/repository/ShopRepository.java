package pbl.GNUB.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pbl.GNUB.entity.Shop;

public interface ShopRepository extends JpaRepository<Shop, Long> {
       List<Shop> findByNameIn(Collection<String> names);

       List<Shop> findTop28ByOrderByLikeCountDesc();

       Shop getShopById(Long id);

       @Query("SELECT s FROM Shop s WHERE s.lat BETWEEN :swLat AND :neLat AND s.lng BETWEEN :swLng AND :neLng")
       List<Shop> findShopsByBounds(@Param("swLat") Double swLat, @Param("neLat") Double neLat,
                     @Param("swLng") Double swLng, @Param("neLng") Double neLng);

       @Query("SELECT DISTINCT s FROM Shop s " +
                     "LEFT JOIN s.shopMenus m " +
                     "WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
                     "LOWER(m.menuName) LIKE LOWER(CONCAT('%', :query, '%'))")
       List<Shop> searchShops(@Param("query") String query);

       @Query("SELECT s FROM Shop s")
       @EntityGraph(attributePaths = "shopTags")
       List<Shop> findAllWithTags();
}
