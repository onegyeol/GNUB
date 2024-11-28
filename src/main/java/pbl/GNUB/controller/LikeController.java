package pbl.GNUB.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import pbl.GNUB.dto.LikeDto;
import pbl.GNUB.service.LikeService;

@Controller
public class LikeController {
    
    @Autowired
    private LikeService likeService;

    @PostMapping("/toggleLike")
public String toggleLike(@RequestParam("shopId") Long shopId, HttpSession session, HttpServletRequest request) {
    String email = (String) session.getAttribute("loginEmail");
    if (email == null) {
        return "redirect:/member/login";
    }

    LikeDto likeDto = new LikeDto();
    likeDto.setShopId(shopId);
    likeDto.setEmail(email);

    likeService.toggleLike(likeDto);
    String referer = request.getHeader("Referer");
    return "redirect:" + referer;
    }

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
}
