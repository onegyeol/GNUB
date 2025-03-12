package pbl.GNUB.dto;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pbl.GNUB.entity.Board;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BoardDto {
    private Long authorId;
    private String authorName;
    private Long id;
    private String title;
    private String content;
    private int boardHits;
    private String boardCreatedTime;
    private String boardUpdatedTime;

    public static BoardDto toBoardDTO(Board board) {
        BoardDto boardDTO = new BoardDto();
        boardDTO.setAuthorId(board.getAuthor() != null ? board.getAuthor().getId() : null);
        boardDTO.setAuthorName(board.getAuthor() != null ? board.getAuthor().getName() : "작성자 미상");
        boardDTO.setId(board.getId());
        boardDTO.setTitle(board.getTitle());
        
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