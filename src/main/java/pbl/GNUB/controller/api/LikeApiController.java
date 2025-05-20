package pbl.GNUB.controller.api;

import java.security.Principal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pbl.GNUB.dto.LikeDto;
import pbl.GNUB.service.LikeService;

@RestController
@RequestMapping("/api/like")
public class LikeApiController {

    @Autowired
    private LikeService likeService;

    @PostMapping("/toggle")
    public ResponseEntity<?> toggleLike(@RequestBody LikeDto likeDto, Principal principal) {
        System.out.println("✅ 받은 shopId: " + likeDto.getShopId());
        
        if (principal == null) {
            return ResponseEntity.status(401).body("로그인 필요");
        }

        likeDto.setEmail(principal.getName());

        boolean liked = likeService.toggleLike(likeDto);
        int likeCount = likeService.getLikeCount(likeDto.getShopId());

        return ResponseEntity.ok(Map.of(
            "liked", liked,
            "likeCount", likeCount
        ));
    }

    @GetMapping("/count")
    public ResponseEntity<?> getLikeCount(@RequestParam Long shopId) {
        int likeCount = likeService.getLikeCount(shopId);
        return ResponseEntity.ok(Map.of("count", likeCount));
    }

}


