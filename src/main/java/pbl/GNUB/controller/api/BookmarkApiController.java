package pbl.GNUB.controller.api;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import pbl.GNUB.entity.BookmarkFolder;
import pbl.GNUB.service.BookmarkService;
import pbl.GNUB.service.MemberService;

@RestController
@RequestMapping("/api/bookmarks")
@RequiredArgsConstructor
public class BookmarkApiController {

    private final BookmarkService bookmarkService;
    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<Void> bookmarkShop(Principal principal,
                                             @RequestParam Long shopId,
                                             @RequestParam(required = false) Long folderId) {
        String email = principal.getName();
        Long memberId = memberService.getMemberEntityByEmail(email).getId();
        bookmarkService.bookmarkShop(memberId, shopId, folderId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> unbookmarkShop(Principal principal,
                                               @RequestParam Long shopId) {
        String email = principal.getName();
        Long memberId = memberService.getMemberEntityByEmail(email).getId();
        bookmarkService.unbookmarkShop(memberId, shopId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/folders")
    public ResponseEntity<List<BookmarkFolder>> getMyFolders(Principal principal) {
        String email = principal.getName();
        Long memberId = memberService.getMemberEntityByEmail(email).getId();
        List<BookmarkFolder> folders = bookmarkService.getMyFolders(memberId);
        return ResponseEntity.ok(folders);
    }

    @PostMapping("/folders")
    public ResponseEntity<Map<String, Object>> createFolder(Principal principal,
                                                            @RequestParam String name) {
        String email = principal.getName();
        Long memberId = memberService.getMemberEntityByEmail(email).getId();
        BookmarkFolder folder = bookmarkService.createFolder(memberId, name);

        Map<String, Object> response = Map.of(
            "id", folder.getId(),
            "name", folder.getName()
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/folders")
    public ResponseEntity<Void> deleteFolder(Principal principal,
                                             @RequestParam String name) {
        String email = principal.getName();
        Long memberId = memberService.getMemberEntityByEmail(email).getId();
        bookmarkService.deleteFolder(memberId, name);
        return ResponseEntity.ok().build();
    }
}
