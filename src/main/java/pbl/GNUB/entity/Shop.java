package pbl.GNUB.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity
@ToString
@Table(name = "SHOP")
public class Shop {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // id

    private String name; // 업소명
    private String category; // 업태
    private String address; // 주소
    private String location;
    private String campus;
    private String addressInfo;
    private String number; // 전화번호
    private String site; // 해당 사이트
    private String info; 

    @Column(length = 1024)  // 이미지 URL 길이를 1024자로 제한
    private String imgUrl; // 이미지 Url

    private String mon;
    private String tue;
    private String wed;
    private String thu;
    private String fri;
    private String sat;
    private String sun; 

    private Double lat; // 위도
    private Double lng; // 경도

    private String restId; // 정문후문 및 캠퍼스 구분 id

    @Builder.Default
    private int likeCount = 0; // 좋아요 수 카운트

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @ToString.Exclude
    @JsonIgnore
    private List<ShopMenu> shopMenus;

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @ToString.Exclude
    @JsonIgnore
    private List<ShopTag> shopTags; // ✅ ShopTag와의 일대다 관계로 수정

    // likeCount setter
    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

}