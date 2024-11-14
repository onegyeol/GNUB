package pbl.GNUB.controller;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import pbl.GNUB.dto.BoardDto;
import pbl.GNUB.dto.MemberFormDto;
import pbl.GNUB.entity.Board;
import pbl.GNUB.entity.Department;
import pbl.GNUB.entity.Member;
import pbl.GNUB.entity.Shop;
import pbl.GNUB.repository.MemberRepository;
import pbl.GNUB.service.BoardService;
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
    private final BoardService boardService;

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
public String showLikedShops(Model model, HttpSession session) {
    String memberEmail = (String) session.getAttribute("loginEmail"); // 세션에서 이메일 가져오기
    List<Shop> likedShops = likeService.getLikedShopsByMemberEmail(memberEmail);
    
    // 로그로 데이터 확인
    if (likedShops.isEmpty()) {
        System.out.println("No liked shops found.");
    } else {
        likedShops.forEach(shop -> System.out.println("Liked Shop: " + shop.getName()));
    }
    
    model.addAttribute("likedShops", likedShops);
    return "form/likeList";
}


   // 내가 작성한 게시글 목록 조회
   @GetMapping("/myPage/myPost")
   public String getMyPosts(HttpSession session, Model model) {
        String loginEmail = (String) session.getAttribute("loginEmail");

        List<Board> boardList = boardService.getBoardsByMemberEmail(loginEmail);
        List<BoardDto> boardDTOList = boardList.stream()
            .map(BoardDto::toBoardDTO) // BoardDto로 변환
            .collect(Collectors.toList());

        // 사용자가 작성한 게시글 조회
        model.addAttribute("myPosts", boardDTOList);
        return "form/myPost"; 
}


    
    
}
