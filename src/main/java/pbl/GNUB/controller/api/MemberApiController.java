package pbl.GNUB.controller.api;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import pbl.GNUB.dto.MemberFormDto;
import pbl.GNUB.service.MemberService;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody MemberFormDto memberFormDto, HttpSession session) {
        MemberFormDto loginResult = memberService.login(memberFormDto);
        if (loginResult != null) {
            List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    loginResult.getEmail(), null, authorities
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // ✅ 여기가 핵심
            session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

            System.out.println("로그인 성공: " + loginResult);
            return ResponseEntity.ok(Map.of("message", "로그인 성공"));
        } else {
            System.out.println("로그인 실패: ");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "로그인 실패"));
        }
    }

    @PostMapping("/new")
    public ResponseEntity<?> signup(@RequestBody MemberFormDto memberFormDto) {
        memberService.save(memberFormDto);
        return ResponseEntity.ok(Map.of("message", "회원가입 성공"));
    }

    @GetMapping("/check-email")
    public ResponseEntity<Map<String, Boolean>> checkEmail(@RequestParam String email) {
        boolean exists = memberService.isEmailDuplicate(email);
        return ResponseEntity.ok(Map.of("exists", exists));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate(); // 세션 제거
        SecurityContextHolder.clearContext(); // SecurityContext 초기화

        return ResponseEntity.ok(Map.of("message", "로그아웃 성공"));
    }

}

