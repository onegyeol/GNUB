package pbl.GNUB.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;
import pbl.GNUB.entity.Bookmark;
import pbl.GNUB.entity.BookmarkFolder;
import pbl.GNUB.service.BookmarkService;

@Controller
@RequestMapping("/bookmarks")
@RequiredArgsConstructor
public class BookmarkController {
    private final BookmarkService bookmarkService;

    // 저장
    @PostMapping
    public ResponseEntity<Void> bookmark(@RequestParam Long memberId,
                                         @RequestParam Long shopId,
                                         @RequestParam(required = false) Long folderId) {
        bookmarkService.bookmarkShop(memberId, shopId, folderId);
        return ResponseEntity.ok().build();
    }

    // 저장 취소
    @DeleteMapping
    public ResponseEntity<Void> unbookmark(@RequestParam Long memberId,
                                           @RequestParam Long shopId) {
        bookmarkService.unbookmarkShop(memberId, shopId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/folder/delete")
    public String deleteBookmarkFolder(@RequestParam Long memberId,
                                    @RequestParam String folderName,
                                    RedirectAttributes redirectAttributes) {
        bookmarkService.deleteFolder(memberId, folderName);

        System.out.println("🔴delete");
        // 삭제 후 리디렉션
        redirectAttributes.addFlashAttribute("message", "폴더가 삭제되었습니다.");
        return "redirect:/myPage/bookmarkList"; // 또는 네가 폴더 리스트 보여주는 경로
    }



    // 찜한 음식점 목록
    @GetMapping("/folders")
    public ResponseEntity<List<BookmarkFolder>> getFolders(@RequestParam Long memberId) {
        List<BookmarkFolder> folders = bookmarkService.getMyFolders(memberId);
        return ResponseEntity.ok(folders);
    }

    

    @PostMapping("/folders")
    public ResponseEntity<Void> createFolder(@RequestParam Long memberId,
                                            @RequestParam String name) {
        bookmarkService.createFolder(memberId, name);
        return ResponseEntity.ok().build();
    }


}
