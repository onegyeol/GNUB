package pbl.GNUB.controller.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pbl.GNUB.dto.MemberFormDto;
import pbl.GNUB.entity.Bookmark;
import pbl.GNUB.entity.Member;
import pbl.GNUB.entity.Shop;
import pbl.GNUB.service.BookmarkService;
import pbl.GNUB.service.LikeService;
import pbl.GNUB.service.MemberService;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/myPage")
@RequiredArgsConstructor
public class MypageApiController {

    private final LikeService likeService;
    private final MemberService memberService;
    private final BookmarkService bookmarkService;

    @GetMapping("/info")
    public ResponseEntity<?> getMyInfo(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body("로그인 필요");
        }

        String loginEmail = principal.getName();
        MemberFormDto memberFormDto = memberService.getMemberInfoByEmail(loginEmail);

        // department 정보도 같이 가져와서 dto 확장하거나, 새로운 Response DTO 만들어도 됨
        return ResponseEntity.ok(memberFormDto);
    }

    @GetMapping("/likes")
    public ResponseEntity<?> getMyLikes(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body("로그인 필요");
        }

        String email = principal.getName();
        List<Shop> likedShops = likeService.getLikedShopsByMemberEmail(email);
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
        return ResponseEntity.ok(grouped);
    }
}
