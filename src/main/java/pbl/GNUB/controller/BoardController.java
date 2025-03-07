package pbl.GNUB.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import pbl.GNUB.dto.BoardDto;
import pbl.GNUB.entity.Board;
import pbl.GNUB.entity.Member;
import pbl.GNUB.repository.MemberRepository;
import pbl.GNUB.service.BoardService;

@Controller
@RequestMapping("/board") // 게시판 관련 요청 처리
@RequiredArgsConstructor // final 필드 자동 생성
public class BoardController {
    private final BoardService boardService;
    private final MemberRepository memberRepository;
    private final String uploadDir = "uploads/";

    @GetMapping("/main")
    public String BoardMain(Model model, @RequestParam(value = "page", defaultValue = "1") int page) {
        int pageSize = 10;
        Page<BoardDto> boardPage = boardService.findPaginated(page, pageSize);

        model.addAttribute("boardList", boardPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", boardPage.getTotalPages());
        model.addAttribute("totalItems", boardPage.getTotalElements());

        return "form/board";
    }

    @PostMapping("/uploadImage")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> uploadImage(@RequestParam("files") MultipartFile[] files) {
        Map<String, Object> response = new HashMap<>();
        List<String> imageUrls = new ArrayList<>();

        if (files.length > 0) {
            try {
                for (MultipartFile file : files) {
                    if (!file.isEmpty()) {
                        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                        Path filePath = Paths.get(uploadDir + fileName);
                        Files.createDirectories(filePath.getParent());
                        Files.write(filePath, file.getBytes());

                        imageUrls.add("/uploads/" + fileName);
                    }
                }
                response.put("imageUrls", imageUrls);
                return ResponseEntity.ok(response);  // ✅ JSON 응답 반환
            } catch (IOException e) {
                e.printStackTrace();
                response.put("error", "파일 업로드 실패");
                return ResponseEntity.status(500).body(response);
            }
        } else {
            response.put("error", "파일이 없습니다.");
            return ResponseEntity.badRequest().body(response);
        }
    }
    

    // 게시글 로딩
    @GetMapping("/load")
    public ResponseEntity<List<Board>> loadMorePosts(@RequestParam int page) {
        int pageSize = 10; // 한 번에 불러올 게시글 수
        List<Board> boards = boardService.getMorePosts(page, pageSize);
        return ResponseEntity.ok(boards);
    }

    // 게시판 작성 화면 접근
    @GetMapping("/write")
    public String BoardWrite() {
        return "form/write";
    }

    // 게시글 작성
    @PostMapping("/write")
    public String Write(@ModelAttribute BoardDto boardDTO, HttpSession session) {
        // 세션에서 loginEmail 가져오기
        String email = (String) session.getAttribute("loginEmail");

        if (email != null) {
            // 이메일로 회원 정보 가져오기
            Member author = memberRepository.findByEmail(email)
                                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));

            boardService.save(boardDTO, author);

            System.out.println("BoardDTO: " + boardDTO);  // DTO 내용 확인
            System.out.println("Author: " + author);  // 작성자 정보 확인
        } else {
            System.out.println("인증되지 않은 사용자입니다.");
        }

        return "redirect:/board/main"; // 게시글 작성 완료 후 이동할 페이지
    }

    // 게시글 상세 페이지
    @GetMapping("/{id}")
    public String BoardPage(@PathVariable("id") Long id, Model model) {
        BoardDto boardDto = boardService.getBoardById(id);

        // Zero Width No-Break Space 제거
        String cleanedContent = boardDto.getContent().replaceAll("\uFEFF", "");
        boardDto.setContent(cleanedContent);

        model.addAttribute("board", boardDto);

        return "form/post";  // 상세 페이지 템플릿
    }

    // 게시글 수정 화면 접근
    @GetMapping("/edit/{id}")
    public String BoardEdit(@PathVariable("id") Long id, Model model) {
        BoardDto boardDto = boardService.getBoardById(id);

        model.addAttribute("board", boardDto);
        return "form/edit";
    }

    // 게시글 수정 요청 처리
    @PostMapping("/edit/{id}")
    public String updateBoard(@PathVariable("id") Long id, @ModelAttribute BoardDto boardDTO) {
        
        // HTML 변환 제거 (그대로 저장)
        boardService.updateBoard(id, boardDTO);

        System.out.println("BoardDTO: " + boardDTO);  // DTO 내용 확인

        return "redirect:/board/" + id;  // 수정 후 상세 페이지로 이동
    }

    // 게시글 삭제 요청 처리
    @PostMapping("/delete/{id}")
    public String deleteBoard(@PathVariable("id") Long id) {
        boardService.deleteBoard(id);
        return "redirect:/board/main";  // 삭제 후 메인 페이지로 이동
    }
}
