package pbl.GNUB.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import pbl.GNUB.dto.MemberFormDto;
import pbl.GNUB.entity.Shop;
import pbl.GNUB.service.LikeService;

import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/myPage")
@RequiredArgsConstructor // final 필드에 대한 생성자를 자동으로 생성
public class MypageController {

    @Autowired
    private LikeService likeService;


    @GetMapping
    public String myPage() {
        return "form/myPage";
    }

    
    @GetMapping("/likeList")
    public String getLikedShops(HttpSession session, Model model) {
        String email = (String) session.getAttribute("login"); // 세션에서 로그인된 회원의 이메일을 가져옴
        if (email != null) {
            // 이미 구현된 getLikedShopsByMember 메서드 사용
            List<Shop> likedShops = likeService.getLikedShopsByMember(email); 
            model.addAttribute("likedShops", likedShops); // 모델에 likedShops 추가
        }
        return "form/likeList";
    }

    @GetMapping("/myPost") // 내가 작성한 글 접근
    public String myPost() {
        return "form/myPost";
    }

    
    
}
