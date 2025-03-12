package pbl.GNUB.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.HtmlUtils;

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
    public void save(BoardDto boardDTO, Member author){
        System.out.println("Saving board with author: " + author.getEmail());
        
        // <p> 태그 제거, </p>를 <br>로 변환하여 저장
        String convertedContent = boardDTO.getContent()
                                        .replaceAll("\uFEFF", "")
                                        .replace("<p>", "")
                                        .replace("</p>", "<br>");

        boardDTO.setContent(convertedContent);

        Board board = new Board();
        board.setTitle(boardDTO.getTitle());
        board.setContent(convertedContent);  // 변환된 content 저장
        board.setAuthor(author);

        boardRepository.save(board);
    }

    public List<Board> findAllBoard() {
        return boardRepository.findAll();
    }

    public Page<BoardDto> findPaginated(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<Board> boardPage = boardRepository.findAll(pageable);
        return boardPage.map(BoardDto::toBoardDTO);
    }

    public Page<BoardDto> findPaginatedMyPosts(int page, int pageSize, String email) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<Board> boardPage = boardRepository.findByAuthorEmailPaged(email, pageable);
        return boardPage.map(BoardDto::toBoardDTO);
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

        // HTML Escape 해제 (출력 시 태그 적용되도록)
        String unescapedContent = HtmlUtils.htmlUnescape(board.getContent());

        BoardDto boardDto = BoardDto.toBoardDTO(board);
        boardDto.setContent(unescapedContent); // 변환된 html 적용

        return boardDto;
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

        // 수정 시 기존 변환 로직 유지
        String convertedContent = boardDTO.getContent()
                                        .replaceAll("\uFEFF", "") 
                                        .replace("<p>", "") 
                                        .replace("</p>", "<br>");

        boardDTO.setContent(convertedContent);

        board.setTitle(boardDTO.getTitle());
        board.setContent(convertedContent);

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

    public List<Board> getMorePosts(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "boardCreatedTime"));
        return boardRepository.findAll(pageable).getContent();
    }
}