package pbl.GNUB.controller.api;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pbl.GNUB.dto.BoardDto;
import pbl.GNUB.service.BoardService;


@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardApiController {

    private final BoardService boardService;

    @GetMapping
    public Page<BoardDto> getBoards(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "sort", defaultValue = "created") String sort,
            @RequestParam(value = "query", required = false) String query) {

        int pageSize = 10;

        if (query != null && !query.isEmpty()) {
            return boardService.searchBoards(query, page, pageSize);
        } else {
            return boardService.findPaginatedWithSort(page, pageSize, sort);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardDto> getBoard(@PathVariable Long id) {
        BoardDto boardDto = boardService.getBoardById(id);

        // Zero Width No-Break Space 제거
        String cleanedContent = boardDto.getContent().replaceAll("\uFEFF", "");
        boardDto.setContent(cleanedContent);

        return ResponseEntity.ok(boardDto);
    }
    
}
