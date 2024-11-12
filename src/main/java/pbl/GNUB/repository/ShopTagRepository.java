package pbl.GNUB.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pbl.GNUB.entity.ShopTag; // ShopTag 엔티티를 import

public interface ShopTagRepository extends JpaRepository<ShopTag, Long> {
    // 기본 CRUD 메서드가 자동으로 제공됩니다.
    // 추가적인 쿼리 메서드를 정의할 수 있습니다.
    List<ShopTag> findTop10ByOrderByIdAsc();

    
    // 추가: name으로 ShopTag를 찾는 메서드
    ShopTag findByName(String name);
}
