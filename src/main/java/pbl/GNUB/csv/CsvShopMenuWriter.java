package pbl.GNUB.csv;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;

import pbl.GNUB.dto.ShopMenuDto;
import pbl.GNUB.entity.ShopMenu;
import pbl.GNUB.repository.ShopMenuRepository;

@Configuration
@RequiredArgsConstructor
public class CsvShopMenuWriter implements ItemWriter<ShopMenuDto>{

    private final ShopMenuRepository shopMenuRepository;

    @Override
    @Transactional
    public void write(@NonNull Chunk<? extends ShopMenuDto> chunk) {
        // DTO를 엔티티로 변환
        List<ShopMenu> shopMenus = chunk.getItems().stream()
                .map(ShopMenuDto::toEntity)
                .toList();

        // Repository를 사용하여 저장
        shopMenuRepository.saveAll(shopMenus);
    }


}
