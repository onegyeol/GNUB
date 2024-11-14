package pbl.GNUB.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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

    @Autowired
    private final BoardRepository boardRepository;

    @Transactional
    public void save(BoardDto boardDTO, Member author) {
        System.out.println("Saving board with author: " + author.getEmail());
        Board board = Board.toSaveEntity(boardDTO, author);
        boardRepository.save(board);
    }

    public List<Board> findAllBoard() {
        return boardRepository.findAll();
    }

    public List<BoardDto> findAllBoardDto() {
        List<Board> boardList = boardRepository.findAll();
        List<BoardDto> boardDTOList = new ArrayList<>();
        for (Board board : boardList) {
            boardDTOList.add(BoardDto.toBoardDTO(board));
        }
        return boardDTOList;
    }

    @Transactional
    public BoardDto getBoardById(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid board Id: " + id));
        increaseBoardHits(board);
        return BoardDto.toBoardDTO(board);
    }

    @Transactional
    public void increaseBoardHits(Board board) {
        board.setBoardHits(board.getBoardHits() + 1);
        boardRepository.save(board);
    }

    @Transactional
    public void updateBoard(Long id, BoardDto boardDTO) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid board Id: " + id));
        board.setTitle(boardDTO.getTitle());
        board.setContent(boardDTO.getContent());
        boardRepository.save(board);
    }

    @Transactional
    public void deleteBoard(Long id) {
        boardRepository.deleteById(id);
    }

    // 내가 작성한 글 확인
    @Transactional(readOnly = true)
    public List<Board> getBoardsByMemberEmail(String email) {
        return boardRepository.findByAuthorEmail(email);
    }

    
}
