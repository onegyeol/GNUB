package pbl.GNUB.csv;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import pbl.GNUB.dto.ShopTagDto;
import pbl.GNUB.entity.ShopTag;
import pbl.GNUB.repository.ShopTagRepository;


@Configuration
public class CsvShopTagWriter implements ItemWriter<ShopTagDto> {
    
    private final ShopTagRepository shopTagRepository;

    public CsvShopTagWriter(ShopTagRepository shopTagRepository) {
        this.shopTagRepository = shopTagRepository;
    }

    @Override
    @Transactional
    public void write(Chunk<? extends ShopTagDto> items) throws Exception {
        for(ShopTagDto dto : items){
            ShopTag existingTag = shopTagRepository.findByAllFields(
                 dto.getName(), dto.getAlone(), dto.getDate(), dto.getDelicious(), dto.getFresh(),
                 dto.getGoodValue(), dto.getHygiene(), dto.getKindness(), dto.getMany(), dto.getMood(),
                 dto.getParking(), dto.getRecent()
            );
            if(existingTag == null) {
                ShopTag newTag = dto.toEntity();
                shopTagRepository.save(newTag);
            }
        }
    }

}