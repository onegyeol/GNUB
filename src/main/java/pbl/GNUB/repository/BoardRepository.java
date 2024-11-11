package pbl.GNUB.repository;

import pbl.GNUB.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardRepository extends JpaRepository<Board, Long>{
    @Modifying
    @Query("UPDATE Board b SET b.boardHits = b.boardHits + 1 WHERE b.id = :id")
    void increaseBoardHits(@Param("id") Long id);
}
