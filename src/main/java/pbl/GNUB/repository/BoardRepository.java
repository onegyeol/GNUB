package pbl.GNUB.repository;

import pbl.GNUB.dto.BoardDto;
import pbl.GNUB.entity.Board;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardRepository extends JpaRepository<Board, Long>{
    @Modifying
    @Query("UPDATE Board b SET b.boardHits = b.boardHits + 1 WHERE b.id = :id")
    void increaseBoardHits(@Param("id") Long id);

    // 내가 작성한 글 확인
    @Query("SELECT b FROM Board b JOIN b.author m WHERE m.email = :email")
    List<Board> findByAuthorEmail(@Param("email") String email);

}
