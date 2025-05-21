package pbl.GNUB.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import pbl.GNUB.entity.Shop;
import pbl.GNUB.entity.ShopMenu;

@Repository
public interface ShopMenuRepository extends JpaRepository<ShopMenu, Long> {
    List<ShopMenu> findByRestName(String restName);

    @Query("SELECT DISTINCT s FROM Shop s " +
            "LEFT JOIN FETCH s.shopMenus m " +
            "WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "   OR LOWER(s.address) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Shop> searchShopsWithMenus(@Param("query") String query);

    @Query("SELECT m FROM ShopMenu m WHERE m.shop.id IN :shopIds")
    List<ShopMenu> findMenusByShopIds(@Param("shopIds") List<Long> shopIds);

}
