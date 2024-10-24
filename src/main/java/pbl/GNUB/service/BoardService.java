package pbl.GNUB.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import pbl.GNUB.dto.BoardDto;
import pbl.GNUB.entity.Board;
import pbl.GNUB.entity.Member;
import pbl.GNUB.repository.BoardRepository;


// DTO -> Entity (Entity Class)
// Entity -> DTO (DTO Class)


@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    @Transactional
    public void save(BoardDto boardDTO, Member author) {
        // 디버깅용 로그
        System.out.println("Saving board with author: " + author.getEmail());

        Board board = Board.toSaveEntity(boardDTO, author);
        boardRepository.save(board);

    }

    // 모든 게시글을 가져오는 메서드
    public List<Board> findAllBoard() {
        return boardRepository.findAll(); // Board 엔티티 리스트 반환
    }

    public List<BoardDto> findAllBoardDto() {
        List<Board> boardList = boardRepository.findAll();
        List<BoardDto> boardDTOList = new ArrayList<>();
        // entity -> dto 
        for (Board board:boardList){
            boardDTOList.add(BoardDto.toBoardDTO(board));
        }
        return boardDTOList;
    }

}