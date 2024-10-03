package pbl.GNUB.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import pbl.GNUB.dto.MemberFormDto;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequiredArgsConstructor // final 필드에 대한 생성자를 자동으로 생성
public class MypageController {

    @GetMapping("/myPage")
    public String myPage() {
        return "form/myPage";
    }

    @GetMapping("/myPost") // 내가 작성한 글 접근
    public String myPost() {
        return "form/myPost";
    }

    @GetMapping("/likeList") // 내가 좋아요한 가게 접근
    public String myLikeShop() {
        return "form/likeList";
    }

    @GetMapping("/keepList") // 내가 찜한 가게 접근
    public String myKeepShop() {
        return "form/keepList";
    }
    
    
}
