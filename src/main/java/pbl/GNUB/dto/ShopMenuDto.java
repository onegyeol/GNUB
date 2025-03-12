package pbl.GNUB.dto;

import lombok.*;
import pbl.GNUB.entity.ShopMenu;
import io.swagger.annotations.*;;

@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "음식점메뉴정보")
public class ShopMenuDto {
    
    @ApiModelProperty(name="rest_name")
    private String restName;

    @ApiModelProperty(name="menu_name")
    private String menuName;

    @ApiModelProperty(name="price")
    private String price;

    @ApiModelProperty(name="img_url")
    private String imgUrl;

    public ShopMenu toEntity(){ //ShopMenu 엔티티 반환
        return ShopMenu.builder()
            .restName(this.restName)
            .menuName(this.menuName)
            .price(this.price)
            .imgUrl(this.imgUrl)
            .build();
    }
}
