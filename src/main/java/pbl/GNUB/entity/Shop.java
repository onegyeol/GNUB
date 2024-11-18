package pbl.GNUB.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity
@ToString
@Table(name = "SHOP")
public class Shop {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // id

    private String region; // 시도 
    private String city; // 시군구
    private String category; // 업태
    private String mainMenu; // 주메뉴
    private String name; // 업소명
    private String address; // 소재지도
    private String phone; // 전화번호
    private Double lat; // 위도
    private Double lng; // 경도

    @Column(length = 1024)  // 이미지 URL 길이를 1024자로 제한
    private String imgUrl; // 이미지 Url
    
    private int likeCount=0; // 좋아요 수 카운트

    @ManyToMany
    @JoinTable(
        name = "SHOP_SHOPTAG",
        joinColumns = @JoinColumn(name = "shop_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<ShopTag> shopTags;

     // likeCount setter
     public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }
}
