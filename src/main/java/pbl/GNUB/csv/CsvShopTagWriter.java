package pbl.GNUB.csv;

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
            ShopTag existingTag = shopTagRepository.findByName(dto.getName());
            if (existingTag == null) {
                ShopTag shopTag = dto.toEntity();
                shopTagRepository.save(shopTag);
            } else {
                // 기존 데이터 업데이트 로직
                updateExistingTag(existingTag, dto);
            }
        }
    }

private void updateExistingTag(ShopTag existingTag, ShopTagDto dto) {
    existingTag.setHygiene(dto.getHygiene());
    existingTag.setRevisit(dto.getRevisit());
    // ... 다른 필드들도 업데이트
    shopTagRepository.save(existingTag);
}
}