package pbl.GNUB.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pbl.GNUB.entity.Shop;

@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "음식점정보")
public class ShopDto {

    @ApiModelProperty(name = "시도")
    private String region; // 시도 

    @ApiModelProperty(name = "시군구")
    private String city; // 시군구

    @ApiModelProperty(name = "업태")
    private String category; // 업태

    @ApiModelProperty(name = "주메뉴")
    private String mainMenu; // 주메뉴

    @ApiModelProperty(name = "업소명")
    private String name; // 업소명

    @ApiModelProperty(name = "소재지도")
    @Column(columnDefinition = "VARCHAR(255) CHARACTER SET utf8mb4")
    private String address; // 주소

    @ApiModelProperty(name = "전화번호")
    private String phone; // 전화번호

    @ApiModelProperty(name = "위도")
    private Double lat; // 위도

    @ApiModelProperty(name = "경도")
    private Double lng; // 경도

    @ApiModelProperty(name = "img_url")
    @Column(columnDefinition = "TEXT")
    private String imgUrl;

    public Shop toEntity(){ //Shop 엔티티 반환
        return Shop.builder()
            .region(this.region)
            .city(this.city)
            .category(this.category)
            .mainMenu(this.mainMenu)
            .name(this.name)
            .address(this.address)
            .phone(this.phone)
            .lat(this.lat)
            .lng(this.lng)
            .imgUrl(this.imgUrl)
            .build();
    }

}
