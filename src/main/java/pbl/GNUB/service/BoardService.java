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
        for (Board board : boardList) {
            boardDTOList.add(BoardDto.toBoardDTO(board));
        }
        return boardDTOList;
    }

    // 특정 게시글 조회
    @Transactional
    public BoardDto getBoardById(Long id) {
        // 게시글 엔티티 조회
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid board Id: " + id));

        // 조회수 증가
        increaseBoardHits(board);

        // Board 엔티티를 BoardDto로 변환하여 반환
        return BoardDto.toBoardDTO(board);
    }

    // 조회수 증가
    @Transactional
    public void increaseBoardHits(Board board) {
        board.setBoardHits(board.getBoardHits() + 1);
        boardRepository.save(board);  // 업데이트된 조회수를 DB에 저장
    }

    // 수정 메서드
    @Transactional
    public void updateBoard(Long id, BoardDto boardDTO) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid board Id: " + id));
        
        board.setTitle(boardDTO.getTitle());
        board.setContent(boardDTO.getContent());
        boardRepository.save(board);  // 수정된 내용 저장
    }

    // 삭제 메서드
    @Transactional
    public void deleteBoard(Long id) {
        boardRepository.deleteById(id);  // 게시글 삭제
    }
}
