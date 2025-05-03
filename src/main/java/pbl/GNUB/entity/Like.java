package pbl.GNUB.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "LIKES")
@Getter
@Setter
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Shop과의 다대일 관계 설정
    @ManyToOne
    @JoinColumn(name = "shop_id") // shopId 필드가 Shop 엔티티의 id 필드를 참조하도록 설정
    private Shop shop;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member; // 누가 좋아요를 눌렀는지

    private String email; // 회원 email

}