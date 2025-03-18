package pbl.GNUB.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pbl.GNUB.entity.ShopMenu;

@Repository
public interface ShopMenuRepository extends JpaRepository<ShopMenu, Long> {
    List<ShopMenu> findByRestName(String restName);
}

