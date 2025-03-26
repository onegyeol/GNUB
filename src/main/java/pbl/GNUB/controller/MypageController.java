package pbl.GNUB.controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import pbl.GNUB.dto.BoardDto;
import pbl.GNUB.dto.MemberFormDto;
import pbl.GNUB.entity.Bookmark;
import pbl.GNUB.entity.Department;
import pbl.GNUB.entity.Member;
import pbl.GNUB.entity.Shop;
import pbl.GNUB.service.BoardService;
import pbl.GNUB.service.BookmarkService;
import pbl.GNUB.service.LikeService;
import pbl.GNUB.service.MemberService;

@Controller
@RequiredArgsConstructor
public class MypageController {

    private final LikeService likeService;
    private final MemberService memberService;
    private final BookmarkService bookmarkService;

    @GetMapping("/myPage")
    public String showMyPage(Principal principal, Model model) {
        if(principal == null) return "redirect:/member/login";

        if (principal != null) {
            System.out.println("📍 로그인됨 : "+principal.getName());
            String loginEmail = principal.getName();
            MemberFormDto memberFormDto = memberService.getMemberInfoByEmail(loginEmail);
            Long departmentId = memberFormDto.getDepartmentId();

            Department department = memberService.getDepartmentById(departmentId);
            model.addAttribute("departmentName", department.getName());
            model.addAttribute("departmentCollege", department.getCollege().getKoreanName());
            model.addAttribute("member", memberFormDto);
        }
        return "form/myPage";
    }

    @GetMapping("/myPage/likeList")
    public String showLikedShops(Principal principal, Model model) {
        if(principal == null) return "redirect:/member/login";
        
        if (principal != null) {
            String memberEmail = principal.getName();
            List<Shop> likedShops = likeService.getLikedShopsByMemberEmail(memberEmail);
            model.addAttribute("likedShops", likedShops);
        }
        return "form/likeList";
    }

    @GetMapping("/myPage/bookmarkList")
    public String bookmarkPage(Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/member/login";
        }

        String loginEmail = principal.getName();
        Member member = memberService.getMemberEntityByEmail(loginEmail);
        Long memberId = member.getId();

        Map<String, List<Bookmark>> grouped = bookmarkService.getGroupedBookmarks(memberId);
        model.addAttribute("member", member);
        model.addAttribute("groupedBookmarks", grouped);

        return "form/bookmarkList";
    }



}
