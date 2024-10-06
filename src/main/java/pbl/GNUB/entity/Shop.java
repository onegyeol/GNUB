package pbl.GNUB.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    
}
