package pbl.GNUB.entity;

import jakarta.persistence.Column;
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

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity
@ToString
@Table(name = "SHOPTAG")
@Builder
public class ShopTag {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // id

    private String name; // 업소명
    private boolean hygiene; // 위생이 좋은
    private boolean revisit; // 재방문률이 높은
    private boolean recent; // 최근에 자주 가는
    private boolean delicious; // 맛있는
    private boolean goodValue; // 가성비 좋은
    private boolean mood; // 깔끔하고 분위기 좋은
    private double fresh; // 신선한
    private double kindness; // 친절한
    private double alone; // 혼밥
    private double chilamDong; // 칠암동
    private double gajwaDong; // 가좌동

    }
