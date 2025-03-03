package pbl.GNUB.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

            // HTML 변환 제거 (그대로 저장)
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

        return "redirect:/board/" + id;  // 수정 후 상세 페이지로 이동
    }

    // 게시글 삭제 요청 처리
    @PostMapping("/delete/{id}")
    public String deleteBoard(@PathVariable("id") Long id) {
        boardService.deleteBoard(id);
        return "redirect:/board/main";  // 삭제 후 메인 페이지로 이동
    }
}
