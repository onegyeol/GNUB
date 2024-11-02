package pbl.GNUB.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import jakarta.servlet.http.HttpSession;
import pbl.GNUB.dto.LikeDto;
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
    public RedirectView likeShop(@RequestParam("shopId") Long shopId, HttpSession session) {
        LikeDto likeDto = getLikeDto(shopId, session);
        if (likeDto == null) {
            return new RedirectView("/member/login"); // 로그인 필요 시 로그인 페이지로 리다이렉트
        }

        int result = likeService.likeShop(likeDto);
        RedirectView redirectView = new RedirectView("/main"); // /main으로 리다이렉트 설정
        if (result > 0) {
            redirectView.addStaticAttribute("message", "좋아요 되었습니다.");
        } else {
            redirectView.addStaticAttribute("message", "이미 좋아요한 음식점입니다.");
        }
        return redirectView;
    }

    @PostMapping("/unlike")
    public RedirectView unlikeShop(@RequestParam("shopId") Long shopId, HttpSession session) {
        LikeDto likeDto = getLikeDto(shopId, session);
        if (likeDto == null) {
            return new RedirectView("/member/login"); // 로그인 필요 시 로그인 페이지로 리다이렉트
        }

        int result = likeService.unlikeShop(likeDto);
        RedirectView redirectView = new RedirectView("/main"); // /main으로 리다이렉트 설정
        if (result > 0) {
            redirectView.addStaticAttribute("message", "좋아요가 취소되었습니다.");
        } else {
            redirectView.addStaticAttribute("message", "좋아요가 취소되지 않았습니다.");
        }
        return redirectView;
    }
}
