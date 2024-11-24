package pbl.GNUB.controller;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import pbl.GNUB.dto.BoardDto;
import pbl.GNUB.entity.Member;
import pbl.GNUB.repository.MemberRepository;
import pbl.GNUB.service.BoardService;


@Controller
@RequestMapping("/board") // /board 경로로 들어오는 요청을 처리하는 컨트롤러 (게시판 관련된 url 매핑 처리)
@RequiredArgsConstructor // final 필드에 대한 생성자를 자동으로 생성
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
    // 게시판 작성 화면 접근
    @GetMapping("/write")
    public String BoardWrite() {
        return "form/write";
    }

    @PostMapping("/write")
    public String Write(@ModelAttribute BoardDto boardDTO, HttpSession session) {
        // 세션에서 loginEmail 가져오기
        String email = (String) session.getAttribute("loginEmail");
        
        if (email != null) {
            // 이메일로 회원 정보를 가져오기
            Member author = memberRepository.findByEmail(email)
                                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));

            // 게시글 저장
            boardService.save(boardDTO, author);
            System.out.println("BoardDTO: " + boardDTO);  // DTO 내용 확인
            System.out.println("Author: " + author);  // 작성자 정보 확인
        } else {
            System.out.println("인증되지 않은 사용자입니다.");
        }

        return "redirect:/board/main"; // 게시글 작성 완료 후 이동할 페이지
    }


    @GetMapping("/{id}")
    public String BoardPage(@PathVariable("id") Long id, Model model) {
        // 게시글 조회 (예: BoardService에서 조회)
        BoardDto boardDto = boardService.getBoardById(id);
        
        // 모델에 게시글 데이터 추가
        model.addAttribute("board", boardDto);

        // 상세 페이지로 이동
        return "form/post";  // 'boardDetail'은 상세 페이지를 렌더링할 Thymeleaf 템플릿 이름
    }


    // 게시글 수정 화면 접근
    @GetMapping("/edit/{id}")
    public String BoardEdit(@PathVariable("id") Long id, Model model) {
        BoardDto boardDto = boardService.getBoardById(id);  // 기존 게시글 내용 가져오기
        model.addAttribute("board", boardDto);
        return "form/edit";  // 게시글 수정 화면으로 이동
    }

    // 게시글 수정 요청 처리
    @PostMapping("/edit/{id}")
    public String updateBoard(@PathVariable("id") Long id, @ModelAttribute BoardDto boardDTO) {
        boardService.updateBoard(id, boardDTO);  // 수정된 내용 저장
        return "redirect:/board/" + id;  // 수정 후 상세 페이지로 이동
    }

    // 게시글 삭제 요청 처리
    @PostMapping("/delete/{id}")
    public String deleteBoard(@PathVariable("id") Long id) {
        boardService.deleteBoard(id);  // 게시글 삭제
        return "redirect:/board/main";  // 삭제 후 메인 페이지로 이동
    }
}