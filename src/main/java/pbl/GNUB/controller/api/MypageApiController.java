package pbl.GNUB.controller.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import pbl.GNUB.dto.BookmarkDto;
import pbl.GNUB.dto.MemberFormDto;
import pbl.GNUB.dto.MemberInfoResponseDto;
import pbl.GNUB.entity.Bookmark;
import pbl.GNUB.entity.Department;
import pbl.GNUB.entity.Member;
import pbl.GNUB.entity.Shop;
import pbl.GNUB.service.BookmarkService;
import pbl.GNUB.service.LikeService;
import pbl.GNUB.service.MemberService;
import pbl.GNUB.service.ShopService;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/myPage")
@RequiredArgsConstructor
public class MypageApiController {

    private final LikeService likeService;
    private final MemberService memberService;
    private final BookmarkService bookmarkService;
    private final ShopService shopService;

    @GetMapping("/info")
    public ResponseEntity<?> getMyInfo(Principal principal) {
        if (principal == null) {
            System.out.println("[INFO] 로그인 안 된 사용자 요청 - /api/myPage/info");
            return ResponseEntity.status(401).body("로그인 필요");
        }

        String loginEmail = principal.getName();
        MemberFormDto memberFormDto = memberService.getMemberInfoByEmail(loginEmail);
        Long departmentId = memberFormDto.getDepartmentId();
        Department department = memberService.getDepartmentById(departmentId);

        MemberInfoResponseDto responseDto = MemberInfoResponseDto.builder()
            .name(memberFormDto.getName())
            .email(memberFormDto.getEmail())
            .departmentName(department.getName())
            .departmentCollege(department.getCollege().getKoreanName())
            .build();

        System.out.println("[INFO] 반환할 회원 정보: " + responseDto);

        return ResponseEntity.ok(responseDto);
    }


    @GetMapping("/likes")
    public ResponseEntity<?> getMyLikes(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body("로그인 필요");
        }

        String email = principal.getName();
        List<Shop> likedShops = likeService.getLikedShopsByMemberEmail(email);
        System.out.println("🔴likedShops : " + likedShops);
        return ResponseEntity.ok(likedShops);
    }

    @GetMapping("/bookmarks")
    public ResponseEntity<?> getMyBookmarks(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body("로그인 필요");
        }

        String email = principal.getName();
        Member member = memberService.getMemberEntityByEmail(email);
        Map<String, List<Bookmark>> grouped = bookmarkService.getGroupedBookmarks(member.getId());

        // ✅ DTO 변환
        Map<String, List<BookmarkDto>> groupedDto = new HashMap<>();
        for (Map.Entry<String, List<Bookmark>> entry : grouped.entrySet()) {
            List<BookmarkDto> dtoList = entry.getValue().stream()
                    .map(BookmarkDto::from)
                    .toList();
            groupedDto.put(entry.getKey(), dtoList);
        }

        return ResponseEntity.ok(groupedDto);
    }

}
