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

    @ApiModelProperty(name = "id")
    private Long id; 

    @ApiModelProperty(name = "rest")
    private String name; // 업소명 

    @ApiModelProperty(name = "type")
    private String category; // 업태

    @ApiModelProperty(name = "address")
    @Column(columnDefinition = "VARCHAR(255) CHARACTER SET utf8mb4")
    private String address; // 주소

    @ApiModelProperty(name = "SET")
    private String location; //위치 구분 

    @ApiModelProperty(name = "캠퍼스")
    private String campus; // 캠퍼스 구분 (가좌정문/후문, 칠암정문/후문)

    @ApiModelProperty(name = "address_info")
    private String addressInfo; // 주메뉴

    @ApiModelProperty(name = "number")
    private String number; // 전화번호

    @ApiModelProperty(name = "site")
    private String site; // 해당 사이트

    @ApiModelProperty(name = "info")
    private String info; // 가게 정보

    @ApiModelProperty(name = "img_url")
    @Column(columnDefinition = "TEXT")
    private String imgUrl;

    @ApiModelProperty(name = "Mon")
    private String mon; // 월요일

    @ApiModelProperty(name = "Tue")
    private String tue; // 화요일

    @ApiModelProperty(name = "Wed")
    private String wed; // 수요일

    @ApiModelProperty(name = "Thu")
    private String thu; // 목요일

    @ApiModelProperty(name = "Fri")
    private String fri; // 금요일

    @ApiModelProperty(name = "Sat")
    private String sat; // 토요일

    @ApiModelProperty(name = "Sun")
    private String sun; // 일요일

    @ApiModelProperty(name = "lat")
    private Double lat; // 위도

    @ApiModelProperty(name = "lng")
    private Double lng; // 경도

    @ApiModelProperty(name = "Rest_id")
    private String restId;

    private int likeCount;
    
    public Shop toEntity(){ //Shop 엔티티 반환
        return Shop.builder()
            .name(this.name)
            .category(this.category)
            .address(this.address)
            .location(this.location)
            .campus(this.campus)
            .addressInfo(this.addressInfo)
            .number(this.number)
            .site(this.site)
            .info(this.info)
            .imgUrl(this.imgUrl)
            .mon(this.mon)
            .tue(this.tue)
            .wed(this.wed)
            .thu(this.thu)
            .fri(this.fri)
            .sat(this.sat)
            .sun(this.sun)
            .lat(this.lat)
            .lng(this.lng)
            .restId(this.restId)
            .build();
    }

    public ShopDto(Shop shop) {
        this.id = shop.getId(); 
        this.name = shop.getName();
        this.category = shop.getCategory();
        this.address = shop.getAddress();
        this.location = shop.getLocation();
        this.campus = shop.getCampus();
        this.addressInfo = shop.getAddressInfo();
        this.number = shop.getNumber();
        this.site = shop.getSite();
        this.info = shop.getInfo();
        this.imgUrl = shop.getImgUrl();
        this.mon = shop.getMon();
        this.tue = shop.getTue();
        this.wed = shop.getWed();
        this.thu = shop.getThu();
        this.fri = shop.getFri();
        this.sat = shop.getSat();
        this.sun = shop.getSun();
        this.lat = shop.getLat();
        this.lng = shop.getLng();
        this.restId = shop.getRestId();
        this.likeCount = shop.getLikeCount();
    }
    

}
