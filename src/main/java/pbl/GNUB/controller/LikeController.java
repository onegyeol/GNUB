package pbl.GNUB.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import pbl.GNUB.dto.LikeDto;
import pbl.GNUB.entity.Like;
import pbl.GNUB.entity.Shop;
import pbl.GNUB.service.LikeService;

@Controller
public class LikeController {
    
    @Autowired
    private LikeService likeService;

    @PostMapping("/like")
    public ResponseEntity<String> likeShop(@RequestParam Long shopId, HttpSession session){
        String email = (String) session.getAttribute("login"); // 세션에서 이메일 가져오기

        if (email == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        LikeDto likeDto = new LikeDto();
        likeDto.setShopId(shopId);
        likeDto.setEmail(email);

        int result = likeService.likeShop(likeDto);

        if (result > 0) {
            return ResponseEntity.ok("좋아요 되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 좋아요한 음식점입니다.");
        }
    }

    @PostMapping("/unlike")
    public ResponseEntity<String> unlikeShop(@RequestParam Long shopId, HttpSession session){
        String email = (String) session.getAttribute("login");

        if (email == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        LikeDto likeDto = new LikeDto();
        likeDto.setShopId(shopId);
        likeDto.setEmail(email);

        int result = likeService.unlikeShop(likeDto);

        if (result > 0) {
            return ResponseEntity.ok("좋아요가 취소되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("좋아요가 취소되지 않았습니다.");
        }
    }

    /* 
    // 특정 회원이 좋아요한 음식점 목록을 반환하는 API
    @GetMapping("/likes/member")
    public ResponseEntity<List<Shop>> getLikedShopsByMember(@RequestParam String email) {
        List<Shop> likedShops = likeService.getLikedShopsByMember(email);
        return ResponseEntity.ok(likedShops);
    }
    */

    // 특정 음식점 대한 좋아요 리스트 목록
    @GetMapping("/{shopId}/likes")
    public ResponseEntity<List<Like>> getLikesByShopId(@PathVariable Long shopId){
        List<Like> likes = likeService.getLikesByShopId(shopId);
        return ResponseEntity.ok(likes);
    }
    
    @PostMapping("/like/likeShop")
    public ResponseEntity<?> likeShop(@RequestBody LikeDto likeDto) {
        int result = likeService.likeShop(likeDto);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/like/unlikeShop")
    public ResponseEntity<?> unlikeShop(@RequestBody LikeDto likeDto) {
        int result = likeService.unlikeShop(likeDto);
        return ResponseEntity.ok(result);
    }

    
}
