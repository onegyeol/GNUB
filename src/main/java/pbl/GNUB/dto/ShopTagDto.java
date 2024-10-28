package pbl.GNUB.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pbl.GNUB.entity.Shop; // Shop 엔티티를 import

@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "음식점정보")
public class ShopTagDto {

    
    private Shop shop; // Shop 엔티티 필드 추가(업소명을 shop에서 가져옴)

    @ApiModelProperty(name = "위생이 좋은")
    private String hygiene; // 위생이 좋은

    @ApiModelProperty(name = "재방문률이 높은")
    private String revisit; // 재방문률이 높은

    @ApiModelProperty(name = "최근에 자주 가는")
    private String recent; // 최근에 자주 가는

    @ApiModelProperty(name = "맛있는")
    private String delicious; // 맛있는

    @ApiModelProperty(name = "가성비 좋은")
    private String goodValue; // 가성비 좋은

    @ApiModelProperty(name = "깔끔하고 분위기 좋은")
    private String mood; // 깔끔하고 분위기 좋은

    @ApiModelProperty(name = "신선한")
    private Double fresh; // 신선하넝

    @ApiModelProperty(name = "친절한")
    private Double kindness; // 친절한

    @ApiModelProperty(name = "혼밥")
    private Double alone; // 혼밥
    
    @ApiModelProperty(name = "칠암동")
    private Double chilam_dong; // 칠암

    @ApiModelProperty(name = "가좌동")
    private Double gajwa_dong; // 가좌

    public ShopTag toEntity() { // ShopTag 엔티티 반환
        return ShopTag.builder()
            .name(this.shop.getName()) // Shop 객체의 name 사용
            .hygiene(this.hygiene != null && this.hygiene.equals("1")) // 1일 경우 true
            .revisit(this.revisit != null && this.revisit.equals("1")) // 1일 경우 true
            .recent(this.recent != null && this.recent.equals("1")) // 1일 경우 true
            .delicious(this.delicious != null && this.delicious.equals("1")) // 1일 경우 true
            .goodValue(this.goodValue != null && this.goodValue.equals("1")) // 1일 경우 true
            .mood(this.mood != null && this.mood.equals("1")) // 1일 경우 true
            .fresh(this.fresh != null && this.fresh.equals("1") ? 1.0 : 0.0) // 예시로 변환
            .kindness(this.kindness != null && this.kindness.equals("1") ? 1.0 : 0.0)
            .alone(this.alone != null && this.alone.equals("1") ? 1.0 : 0.0)
            .chilamDong(this.chilam_dong != null && this.chilam_dong.equals("1") ? 1.0 : 0.0)
            .gajwaDong(this.gajwa_dong != null && this.gajwa_dong.equals("1") ? 1.0 : 0.0)
            .build(); // .build() 메서드 추가
    }

}
