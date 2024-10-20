package pbl.GNUB.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pbl.GNUB.entity.Like;
import pbl.GNUB.entity.Member;

import java.util.List;

public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByShopIdAndEmail(Long shopId, String email); // 이미 좋아요햔 음식점인지
    void deleteByShopIdAndEmail(Long shopId, String email); // 좋아요 삭제
    List<Like> findByShopId(Long shopId); // 음식점 좋아요 조회
    List<Like> findByMember(Member member);
}