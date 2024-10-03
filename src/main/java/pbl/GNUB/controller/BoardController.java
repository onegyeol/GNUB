package pbl.GNUB.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
@RequestMapping("/board") // /board 경로로 들어오는 요청을 처리하는 컨트롤러 (게시판 관련된 url 매핑 처리)
@RequiredArgsConstructor // final 필드에 대한 생성자를 자동으로 생성
public class BoardController {

    // 게시판 메인 화면 접근
    @GetMapping("/main")
    public String boardMain() {
        return "form/board";
    }

    // 게시판 작성 화면 접근
    @GetMapping("/write")
    public String boardWrite() {
        return "form/write";
    }

    // 게시판 수정 삭제 화면 접근
    @GetMapping("/edit")
    public String boardEdit() {
        return "form/post";
    }
    
    
    
}
