package pbl.GNUB.controller;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import pbl.GNUB.dto.BoardDto;
import pbl.GNUB.entity.Board;
import pbl.GNUB.entity.Member;
import pbl.GNUB.repository.MemberRepository;
import pbl.GNUB.service.BoardService;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;


@Controller
@RequestMapping("/board") // /board 경로로 들어오는 요청을 처리하는 컨트롤러 (게시판 관련된 url 매핑 처리)
@RequiredArgsConstructor // final 필드에 대한 생성자를 자동으로 생성
public class BoardController {
    private final BoardService boardService;
    private final MemberRepository memberRepository;

    @GetMapping("/main")
    public String boardMain(Model model) {
    // DB에서 전체 게시글 데이터를 가져와서 게시판 main화면에 보여준다.
    List<Board> boardList = boardService.findAllBoard(); // Board 엔티티 리스트 가져오기
    List<BoardDto> boardDTOList = boardList.stream()
        .map(BoardDto::toBoardDTO) // BoardDto로 변환
        .collect(Collectors.toList());
    model.addAttribute("boardList", boardDTOList);
    return "form/board";
}


    // 게시판 작성 화면 접근
    @GetMapping("/write")
    public String boardWrite() {
        return "form/write";
    }

    @PostMapping("/write")
    public String Write(@ModelAttribute BoardDto boardDTO) {
        // 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                String email = ((UserDetails) principal).getUsername(); // 사용자의 이메일 가져오기
    
                // 이메일로 회원 정보를 가져오기
                Member author = memberRepository.findByEmail(email)
                                  .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));
    
                // 게시글 저장
                boardService.save(boardDTO, author);
                System.out.println("BoardDTO: " + boardDTO);  // DTO 내용 확인
                System.out.println("Author: " + author);  // 작성자 정보 확인
            }
        }else{
            System.out.println("인증되지 않은 사용자입니다.");
        }

        return "form/board"; // 게시글 작성 완료 후 이동할 페이지
    }

    // 게시판 수정 삭제 화면 접근
    @GetMapping("/edit")
    public String boardEdit() {
        return "form/post";
    }
}