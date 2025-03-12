package pbl.GNUB.csv;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import pbl.GNUB.dto.ShopDto;
import pbl.GNUB.entity.Shop;
import pbl.GNUB.repository.ShopRepository;

@Configuration
@RequiredArgsConstructor
public class CsvShopWriter implements ItemWriter<ShopDto> {

    private final ShopRepository shopRepository;

    @Override
    @Transactional
    public void write(Chunk<? extends ShopDto> items) throws Exception {
        List<Shop> shopList = new ArrayList<>();

        // items에서 ShopDto를 추출하여 Shop 엔티티로 변환
        for (ShopDto dto : items) {
            //System.out.println("imgUrl: " + dto.getImgUrl()); // 로그로 imgUrl 확인
            Shop shop = dto.toEntity();

            shopList.add(shop);
        }
    

        // Shop 엔티티를 데이터베이스에 저장
        shopRepository.saveAll(shopList);
    }
}
