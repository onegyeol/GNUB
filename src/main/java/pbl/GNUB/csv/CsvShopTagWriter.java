package pbl.GNUB.csv;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import pbl.GNUB.dto.ShopTagDto;
import pbl.GNUB.entity.ShopTag;
import pbl.GNUB.entity.Shop; // Shop 엔티티 import
import pbl.GNUB.repository.ShopTagRepository;
import pbl.GNUB.repository.ShopRepository; // ShopRepository import

@Configuration
@RequiredArgsConstructor
public class CsvShopWriter implements ItemWriter<ShopTagDto> {

    private final ShopTagRepository shopTagRepository;
    private final ShopRepository shopRepository; // ShopRepository 주입

    @Override
    @Transactional
    public void write(Chunk<? extends ShopTagDto> items) throws Exception {
        List<ShopTag> shopTagList = new ArrayList<>();

        // items에서 ShopTagDto를 추출하여 ShopTag 엔티티로 변환
        for (ShopTagDto dto : items) {
            // Shop 객체 조회
            Shop shop = shopRepository.findByName(dto.getShop().getName());
            if (shop == null) { //null일경우 체킁
            // 예외 처리 또는 로깅
                continue; // 또는 예외를 던질 수 있음
            }
            dto.setShop(shop); // Shop 객체 설정
            
            ShopTag shopTag = dto.toEntity(); // DTO를 엔티티로 변환
            shopTagList.add(shopTag);
        }

        // ShopTag 엔티티를 데이터베이스에 저장
        shopTagRepository.saveAll(shopTagList);
    }
}