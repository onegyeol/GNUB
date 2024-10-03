package pbl.GNUB.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import pbl.GNUB.dto.MemberFormDto;

@Controller
@RequestMapping("/member") // /member 경로로 들어오는 요청을 처리하는 컨트롤러
@RequiredArgsConstructor // final 필드에 대한 생성자를 자동으로 생성
public class MemberController {

    // 회원 가입 페이지를 보여주는 메서드
    @GetMapping("/new")
    public String Join(Model model){
        model.addAttribute("memberFormDto", new MemberFormDto()); // 빈 MemberFormDto 객체를 모델에 추가
        return "form/join"; // 회원 가입 폼 뷰를 반환
    }

    // 로그인 페이지를 보여주는 메서드
    @GetMapping("/login")
    public String Login(){
        return "form/login"; // 로그인 폼 뷰를 반환
    }

    // 로그아웃시
    @GetMapping("/logout")
    public String Logout(){
        return "form/main"; // 메인 폼 뷰를 반환
    }
    
}
