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
@ApiModel(value = "음식점태그정보")
public class ShopTagDto {

    @ApiModelProperty(name = "rest")
    private String name;

    @ApiModelProperty(name = "Rest_id")
    private String restId;

    @ApiModelProperty(name = "alone")
    private int alone;

    @ApiModelProperty(name = "date")
    private int date;
    
    @ApiModelProperty(name = "delicious")
    private int delicious;

    @ApiModelProperty(name = "fresh")
    private int fresh;

    @ApiModelProperty(name = "goodValue")
    private int goodValue;

    @ApiModelProperty(name = "hygiene")
    private int hygiene;

    @ApiModelProperty(name = "kindness")
    private int kindness;

    @ApiModelProperty(name = "many")
    private int many;

    @ApiModelProperty(name = "mood")
    private int mood;
    
    @ApiModelProperty(name = "parking")
    private int parking;

    @ApiModelProperty(name = "recent")
    private int recent;


    public ShopTag toEntity() {
        return ShopTag.builder()
            .name(this.name)
            .restId(this.restId)
            .alone(this.alone)
            .date(this.date)
            .delicious(this.delicious)
            .fresh(this.fresh)
            .goodValue(this.goodValue)
            .hygiene(this.hygiene)
            .kindness(this.kindness)
            .many(this.many)
            .mood(this.mood)
            .parking(this.parking)
            .recent(this.recent)
            .build();
    }

    

    public ShopTagDto(ShopTag shopTag) {
        this.name = shopTag.getName();
        this.alone = shopTag.getAlone();
        this.date = shopTag.getDate();
        this.delicious = shopTag.getDelicious();
        this.fresh = shopTag.getFresh();
        this.goodValue = shopTag.getGoodValue();
        this.hygiene = shopTag.getHygiene();
        this.kindness = shopTag.getKindness();
        this.many = shopTag.getMany();
        this.mood = shopTag.getMood();
        this.parking = shopTag.getParking();
        this.recent = shopTag.getRecent();
    }
    
}