package pbl.GNUB.controller;

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

    @PostMapping("/login")
    public String PostLogin(@ModelAttribute MemberFormDto memberFormDto, HttpSession session) {
        System.out.println("이메일: " + memberFormDto.getEmail());
        MemberFormDto loginResult = memberService.login(memberFormDto);
        if (loginResult != null) {
            // 로그인 성공
            session.setAttribute("loginEmail", loginResult.getEmail());

            // 사용자 인증 정보 생성 (ROLE_USER 권한 부여)
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                loginResult.getEmail(),
                null, // 비밀번호는 필요 없으므로 null
                authorities
            );

            // SecurityContext에 Authentication 객체 설정
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 확인용 출력
            Authentication authenticatio = SecurityContextHolder.getContext().getAuthentication();
            System.out.println("Authentication: " + authenticatio);
            System.out.println("Principal: " + authenticatio.getPrincipal());

            return "redirect:/main";
        } else {
            // 로그인 실패
            System.out.println("로그인 실패");
            return "redirect:/member/login?error=true";
        }
    }
  

}