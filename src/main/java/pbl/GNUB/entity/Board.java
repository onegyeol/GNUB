
package pbl.GNUB.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import pbl.GNUB.dto.BoardDto;

@Entity
@Getter
@Setter
@Table(name = "board")
public class Board extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length=20, nullable = false)
    private String title;

    @Lob // ✅ TEXT로 매핑 (대용량 데이터 저장 가능)
    @Column(columnDefinition = "TEXT")
    private String content;
    
    @Column
    private int boardHits=0;  // 조회수

    @ManyToOne(fetch = FetchType.LAZY)  // 작성자와 연관관계 설정
    @JoinColumn(name = "member_id")
    private Member author;  // 작성자 (회원)

    // Author 객체를 반환하는 메서드 추가
    public Member getAuthor() {
        return author;
    }

    public static Board toSaveEntity(BoardDto boardDTO,  Member author){
        Board board = new Board();
        board.setTitle(boardDTO.getTitle());
        board.setContent(boardDTO.getContent());
        board.setBoardHits(0);
        board.setAuthor(author);
        return board;
    }
}
