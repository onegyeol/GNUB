package pbl.GNUB.service;

import java.lang.reflect.Member;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pbl.GNUB.dto.LikeDto;
import pbl.GNUB.entity.Like;
import pbl.GNUB.entity.Shop; // Shop 엔티티를 import
import pbl.GNUB.repository.LikeRepository;
import pbl.GNUB.repository.ShopRepository; // ShopRepository import 추가

@Service
public class LikeService {

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private ShopRepository shopRepository; // ShopRepository 주입

    public int likeShop(LikeDto likeDto) {
        if (likeRepository.existsByShopIdAndEmail(likeDto.getShopId(), likeDto.getEmail())) {
            return 0; // 이미 좋아요한 경우
        }

        Like like = new Like();
        like.setEmail(likeDto.getEmail());

        // shopId로 Shop 객체를 조회하여 설정
        Shop shop = shopRepository.findById(likeDto.getShopId())
                                  .orElseThrow(() -> new RuntimeException("Shop not found"));
        like.setShop(shop); // Shop 객체 설정

        // 좋아요 수 증가
        shop.setLikeCount(shop.getLikeCount() + 1);
        shopRepository.save(shop); // 좋아요 수가 업데이트된 Shop 저장

        likeRepository.save(like);
        return 1; // 좋아요 성공
    }

    public int unlikeShop(LikeDto likeDto) {
        if (!likeRepository.existsByShopIdAndEmail(likeDto.getShopId(), likeDto.getEmail())) {
            return 0; // 좋아요한 경우가 아닐 때
        }

        // 좋아요 삭제
        likeRepository.deleteByShopIdAndEmail(likeDto.getShopId(), likeDto.getEmail());

        // shopId로 Shop 객체를 조회하여 좋아요 수 감소
        Shop shop = shopRepository.findById(likeDto.getShopId())
                                  .orElseThrow(() -> new RuntimeException("Shop not found"));

        // 좋아요 수 감소 (음수가 되지 않도록 0 이상으로 유지)
        shop.setLikeCount(Math.max(shop.getLikeCount() - 1, 0));
        shopRepository.save(shop); // 업데이트된 좋아요 수 저장
        
        return 1; // 좋아요 취소 성공
    }

    // 특정 음식점에 대한 좋아요 목록 조회
    public List<Like> getLikesByShopId(Long shopId) {
        return likeRepository.findByShopId(shopId);
    }
/* 
    // 특정 회원이 좋아요를 누른 음식점 리스트 조회
    public List<Shop> getLikedShopsByMember(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        List<Like> likes = likeRepository.findByMember(member);

        return likes.stream()
                    .map(Like::getShop) // Like 객체에서 Shop 추출
                    .toList();
    }
*/
}