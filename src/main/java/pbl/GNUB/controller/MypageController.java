package pbl.GNUB.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import pbl.GNUB.dto.MemberFormDto;
import pbl.GNUB.entity.Department;
import pbl.GNUB.entity.Member;
import pbl.GNUB.entity.Shop;
import pbl.GNUB.repository.MemberRepository;
import pbl.GNUB.service.LikeService;
import pbl.GNUB.service.MemberService;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import jakarta.servlet.http.HttpSession;


@Controller
@RequiredArgsConstructor // final 필드에 대한 생성자를 자동으로 생성
public class MypageController {

    @Autowired
    private LikeService likeService;

    @Autowired
    private final MemberService memberService;

    @Autowired
    private final MemberRepository memberRepository;

    @GetMapping("/myPage")
    public String showMyPage(HttpSession session, Model model) {
        String loginEmail = (String) session.getAttribute("loginEmail"); // 세션에서 이메일을 가져옴
        if (loginEmail != null) {
            // 로그인된 이메일로 회원 정보를 가져옴
            MemberFormDto memberFormDto = memberService.getMemberInfoByEmail(loginEmail); // 이메일로 Member 정보 가져오기
            Long departmentId = memberFormDto.getDepartmentId(); // MemberFormDto에서 departmentId 추출

            // departmentId를 통해 Department 정보를 가져옴
            Department department = memberService.getDepartmentById(departmentId);
            
            // 모델에 Department 정보 추가
            model.addAttribute("departmentName", department.getName());
            model.addAttribute("departmentCollege", department.getCollege().getKoreanName());

            // 모델에 회원 정보 추가
            model.addAttribute("member", memberFormDto); // 회원 정보 추가
        }

        return "form/myPage"; // myPage 뷰로 이동
    }



    
    @GetMapping("/myPage/likeList")
    public String getLikedShops(HttpSession session, Model model) {
        String email = (String) session.getAttribute("login"); // 세션에서 로그인된 회원의 이메일을 가져옴
        if (email != null) {
            // 이미 구현된 getLikedShopsByMember 메서드 사용
            List<Shop> likedShops = likeService.getLikedShopsByMember(email); 
            model.addAttribute("likedShops", likedShops); // 모델에 likedShops 추가
        }
        return "form/likeList";
    }

    @GetMapping("/myPage/myPost") // 내가 작성한 글 접근
    public String myPost() {
        return "form/myPost";
    }

    
    
}
