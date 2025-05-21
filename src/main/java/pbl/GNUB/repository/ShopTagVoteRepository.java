package pbl.GNUB.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import pbl.GNUB.entity.ShopTagVote;
import pbl.GNUB.entity.Shop;
import pbl.GNUB.entity.Member;
import pbl.GNUB.controller.MapController;

import java.util.List;

public interface ShopTagVoteRepository extends JpaRepository<ShopTagVote, Long> {
    boolean existsByMemberAndShopAndTagName(Member member, Shop shop, String tagName);

    long countByShopAndTagName(Shop shop, String tagName);

    void deleteByMemberAndShopAndTagName(Member member, Shop shop, String tagName);

    List<ShopTagVote> findByShop(Shop shop);
}