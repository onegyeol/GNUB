package pbl.GNUB.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import pbl.GNUB.dto.LikeDto;
import pbl.GNUB.service.LikeService;

@Controller
public class LikeController {

    @Autowired
    private LikeService likeService;

    @PostMapping("/toggleLike")
    public String toggleLike(@RequestParam("shopId") Long shopId, Principal principal, HttpServletRequest request) {
        // 로그인 여부 확인
        if (principal == null) {
            return "redirect:/member/login";
        }

        String email = principal.getName();

        LikeDto likeDto = new LikeDto();
        likeDto.setShopId(shopId);
        likeDto.setEmail(email);

        likeService.toggleLike(likeDto);

        // 요청 이전 페이지로 리다이렉트
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }
}
