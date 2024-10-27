package pbl.GNUB.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import pbl.GNUB.entity.Shop;

public interface ShopRepository extends JpaRepository<Shop, Long>{
    // id가 1부터 30까지인 데이터를 조회
    List<Shop> findByIdBetween(Long startId, Long endId);
    //List<Shop> findTop30ByOrderByLikeCountDesc();  // 좋아요 수 기준으로 30개 가져오기
}
