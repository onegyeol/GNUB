package pbl.GNUB.dto;

import java.time.LocalDateTime;

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

public class BoardDto{
    private Long authorId;
    private Long id;
    private String title;
    private String content;
    private int boardHits;  // 조회수
    private LocalDateTime boardCreatedTime;  // 작성한 시간
    private LocalDateTime boardUpdatedTime;  // 수정한 시간

    public static BoardDto toBoardDTO(Board board){
        BoardDto boardDTO = new BoardDto();
        boardDTO.setAuthorId(board.getAuthor().getId());
        boardDTO.setId(board.getId());
        boardDTO.setTitle(board.getTitle());
        boardDTO.setContent(board.getContent());
        boardDTO.setBoardHits(board.getBoardHits());
        boardDTO.setBoardCreatedTime(board.getCreatedTime());
        boardDTO.setBoardUpdatedTime(board.getUpdatedTime());
        return boardDTO;
    }

}
