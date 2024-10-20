package pbl.GNUB.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;
import pbl.GNUB.dto.LikeDto;
import pbl.GNUB.entity.Shop;
import pbl.GNUB.service.LikeService;

@Controller
public class LikeController {
    
    @Autowired
    private LikeService likeService;

    // 공통 메서드: 세션에서 이메일 가져오기 및 LikeDto 생성
    private LikeDto getLikeDto(Long shopId, HttpSession session) {
        String email = (String) session.getAttribute("loginEmail");
        if (email == null) {
            return null; // 이메일이 없으면 null 반환
        }

        LikeDto likeDto = new LikeDto();
        likeDto.setShopId(shopId);
        likeDto.setEmail(email);
        return likeDto;
    }

    @PostMapping("/like")
    public ResponseEntity<String> likeShop(@RequestParam("shopId") Long shopId, HttpSession session) {
        LikeDto likeDto = getLikeDto(shopId, session);
        if (likeDto == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        int result = likeService.likeShop(likeDto);
        if (result > 0) {
            return ResponseEntity.ok("좋아요 되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 좋아요한 음식점입니다.");
        }
    }

    @PostMapping("/unlike")
    public ResponseEntity<String> unlikeShop(@RequestParam("shopId") Long shopId, HttpSession session) {
        LikeDto likeDto = getLikeDto(shopId, session);
        if (likeDto == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        int result = likeService.unlikeShop(likeDto);
        if (result > 0) {
            return ResponseEntity.ok("좋아요가 취소되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("좋아요가 취소되지 않았습니다.");
        }
    }

}
