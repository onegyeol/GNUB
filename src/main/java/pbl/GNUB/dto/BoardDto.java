package pbl.GNUB.dto;

import java.time.format.DateTimeFormatter;

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
    private Long authorId;       // 작성자 ID
    private String authorName;   // 작성자 이름
    private String authorEmail; 
    private Long id;
    private String title;
    private String content;
    private String summary;
    private String thumbnail;
    private int boardHits;
    private String boardCreatedTime;
    private String boardUpdatedTime;

    public static BoardDto toBoardDTO(Board board, String summary, String thumbnail) {
        BoardDto boardDTO = new BoardDto();
        boardDTO.setAuthorId(board.getAuthor() != null ? board.getAuthor().getId() : null);
        boardDTO.setAuthorName(board.getAuthor() != null ? board.getAuthor().getName() : "작성자 미상");
        boardDTO.setAuthorEmail(board.getAuthor() != null ? board.getAuthor().getEmail() : "이메일 없음");  // ✅ 추가

        boardDTO.setId(board.getId());
        boardDTO.setTitle(board.getTitle());
        boardDTO.setContent(board.getContent()); // HTML 그대로 저장
        boardDTO.setSummary(summary);
        boardDTO.setThumbnail(thumbnail);
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
