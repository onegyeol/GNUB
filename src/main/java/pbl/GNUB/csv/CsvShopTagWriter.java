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
import pbl.GNUB.repository.ShopTagRepository;

@Configuration
@RequiredArgsConstructor
public class CsvShopTagWriter implements ItemWriter<ShopTagDto> {

    private final ShopTagRepository shopTagRepository;

    @Override
    @Transactional
    public void write(Chunk<? extends ShopTagDto> items) throws Exception {
        List<ShopTag> shopTagList = new ArrayList();

        // items에서 ShopTagDto를 추출하여 ShopTag 엔티티로 변환
        for (ShopTagDto dto : items) {
            ShopTag shopTag = dto.toEntity(); // DTO를 엔티티로 변환
            shopTagList.add(shopTag);
        }

        // ShopTag 엔티티를 데이터베이스에 저장
        shopTagRepository.saveAll(shopTagList);
    }
}