package pbl.GNUB.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import pbl.GNUB.entity.Member;
import pbl.GNUB.entity.Shop;
import pbl.GNUB.entity.ShopTagVote;

public interface ShopTagVoteRepository extends JpaRepository<ShopTagVote, Long> {

    // 태그 투표 여부 확인
    boolean existsByMemberAndShopAndTagName(Member member, Shop shop, String tagName);

    // 태그별 투표 수 계산
    long countByShopAndTagName(Shop shop, String tagName);

    // 태그 투표 삭제 (토글 Off용)
    void deleteByMemberAndShopAndTagName(Member member, Shop shop, String tagName);

    // ✅ 로그인 사용자 기준 모든 투표 정보 가져오기 (중복 쿼리 방지용)
    List<ShopTagVote> findByMember(Member member);
}