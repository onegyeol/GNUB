package pbl.GNUB.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pbl.GNUB.entity.ShopTag; 

@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "음식점정보")
public class ShopTagDto {

    @ApiModelProperty(name = "음식점")
    private String name;

    @ApiModelProperty(name = "위생이 좋은")
    private int hygiene;

    @ApiModelProperty(name = "재방문률이 높은")
    private int revisit;

    @ApiModelProperty(name = "최근에 자주 가는")
    private int recent;

    @ApiModelProperty(name = "맛있는")
    private int delicious;

    @ApiModelProperty(name = "가성비 좋은")
    private int goodValue;

    @ApiModelProperty(name = "깔끔하고 분위기 좋은")
    private int mood;

    @ApiModelProperty(name = "신선한")
    private int fresh;

    @ApiModelProperty(name = "친절한")
    private int kindness;

    @ApiModelProperty(name = "혼밥")
    private int alone;
    
    @ApiModelProperty(name = "칠암동")
    private int chilamDong;

    @ApiModelProperty(name = "가좌동")
    private int gajwaDong;

    public ShopTag toEntity() {
        return ShopTag.builder()
            .name(this.name)
            .hygiene(this.hygiene)
            .revisit(this.revisit)
            .recent(this.recent)
            .delicious(this.delicious)
            .goodValue(this.goodValue)
            .mood(this.mood)
            .fresh(this.fresh)
            .kindness(this.kindness)
            .alone(this.alone)
            .chilamDong(this.chilamDong)
            .gajwaDong(this.gajwaDong)
            .build();
    }

}
