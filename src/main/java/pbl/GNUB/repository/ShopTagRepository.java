package pbl.GNUB.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pbl.GNUB.entity.ShopTag; // ShopTag 엔티티를 import

public interface ShopTagRepository extends JpaRepository<ShopTag, Long> {
    // 기본 CRUD 메서드가 자동으로 제공됩니다.
    // 추가적인 쿼리 메서드를 정의할 수 있습니다.
}
