package pbl.GNUB.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pbl.GNUB.entity.Board;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BoardDto {
    private Long authorId;  // 작성자 ID
    private String authorName; // 작성자 이름 추가
    private Long id;
    private String title;
    private String content;
    private int boardHits;  // 조회수
    private String boardCreatedTime;  // 날짜를 String으로 변경
    private String boardUpdatedTime;  // 날짜를 String으로 변경

    public static BoardDto toBoardDTO(Board board) {
        BoardDto boardDTO = new BoardDto();
        boardDTO.setAuthorId(board.getAuthor() != null ? board.getAuthor().getId() : null);
        boardDTO.setAuthorName(board.getAuthor() != null ? board.getAuthor().getName() : "작성자 미상");
        boardDTO.setId(board.getId());
        boardDTO.setTitle(board.getTitle());
        
        // HTML 태그를 제거하고 텍스트만 저장
        String cleanContent = Jsoup.parse(board.getContent()).text();
        boardDTO.setContent(cleanContent);

        boardDTO.setBoardHits(board.getBoardHits());
        
        if (board.getCreatedTime() != null) {
            boardDTO.setBoardCreatedTime(board.getCreatedTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        if (board.getUpdatedTime() != null) {
            boardDTO.setBoardUpdatedTime(board.getUpdatedTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }

        return boardDTO;
    }
    
}
