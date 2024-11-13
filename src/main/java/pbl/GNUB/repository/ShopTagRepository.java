package pbl.GNUB.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pbl.GNUB.entity.ShopTag; // ShopTag 엔티티를 import

public interface ShopTagRepository extends JpaRepository<ShopTag, Long> {
    // 값이 1인 ShopTag 리스트를 반환
    List<ShopTag> findByValue(Integer value);
}
