package pbl.GNUB.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import pbl.GNUB.entity.ShopMenu;

public interface ShopMenuRepository extends JpaRepository<ShopMenu, Long> {

    // 메뉴 이름에 특정 키워드가 포함된 음식점 메뉴 조회
    List<ShopMenu> findByMenuNameContainingIgnoreCase(String menuName);

    // 음식점 이름(=restName) 기준으로 메뉴 조회
    List<ShopMenu> findByRestName(String restName);

}
