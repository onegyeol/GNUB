package pbl.GNUB.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import pbl.GNUB.dto.MemberFormDto;
import pbl.GNUB.entity.College;
import pbl.GNUB.entity.Department;
import pbl.GNUB.entity.Member;
import pbl.GNUB.service.MemberService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequestMapping("/member") // /member 경로로 들어오는 요청을 처리하는 컨트롤러
@RequiredArgsConstructor // final 필드에 대한 생성자를 자동으로 생성
public class MemberController {

    private final MemberService memberService;

    // 회원 가입 페이지를 보여주는 메서드
    @GetMapping("/new")
    public String Join(Model model){
        model.addAttribute("memberFormDto", new MemberFormDto()); // 빈 MemberFormDto 객체를 모델에 추가
        return "form/join"; // 회원 가입 폼 뷰를 반환
    }

    
    @PostMapping("/new")
    public String PostJoin(@ModelAttribute MemberFormDto memberFormDto) {
        System.out.println("MemberController.save");
        System.out.println("회원가입 정보: " + memberFormDto);
        memberService.save(memberFormDto);
        return "redirect:/main";
    }

    // 이메일 중복 확인 엔드포인트
    @GetMapping("/check-email")
    @ResponseBody
    public Map<String, Boolean> checkEmailDuplicate(@RequestParam("email") String email) {
        boolean exists = memberService.isEmailDuplicate(email);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return response;
    }
    

    // 로그인 페이지를 보여주는 메서드
    @GetMapping("/login")
    public String Login(){
        return "form/login"; // 로그인 폼 뷰를 반환
    }  

}