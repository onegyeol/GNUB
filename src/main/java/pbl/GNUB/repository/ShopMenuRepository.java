package pbl.GNUB.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import pbl.GNUB.entity.ShopMenu;

public interface ShopMenuRepository extends JpaRepository<ShopMenu, Long> {
    List<ShopMenu> findByShop_Name(String shopName);
}

