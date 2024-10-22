package pbl.GNUB.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import pbl.GNUB.dto.BoardDTO;
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
    public void save(BoardDTO boardDTO, Member author) {
        // 디버깅용 로그
        System.out.println("Saving board with author: " + author.getEmail());

        Board board = Board.toSaveEntity(boardDTO, author);
        boardRepository.save(board);

    }

    public List<BoardDTO> findAll() {
        List<Board> boardList = boardRepository.findAll();
        List<BoardDTO> boardDTOList = new ArrayList<>();
        // entity -> dto 
        for (Board board:boardList){
            boardDTOList.add(BoardDTO.toBoardDTO(board));
        }
        return boardDTOList;
    }

}
