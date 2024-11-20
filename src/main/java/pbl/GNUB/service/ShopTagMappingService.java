package pbl.GNUB.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import pbl.GNUB.entity.Shop;
import pbl.GNUB.entity.ShopTag;
import pbl.GNUB.repository.ShopRepository;
import pbl.GNUB.repository.ShopTagRepository;

@Service
public class ShopTagMappingService {

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private ShopTagRepository shopTagRepository;

    @Transactional
    public void mapShopAndShopTagsById() {
        // 모든 Shop과 ShopTag 조회
        List<Shop> shops = shopRepository.findAll();
        List<ShopTag> shopTags = shopTagRepository.findAll();

        // id가 같은 Shop과 ShopTag를 매핑
        for (Shop shop : shops) {
            for (ShopTag tag : shopTags) {
                if (shop.getId().equals(tag.getId())) {
                    // 관계 설정
                    shop.getShopTags().add(tag);
                    tag.getShops().add(shop);
                }
            }
        }

        // 저장 (연관 관계가 업데이트됨)
        shopRepository.saveAll(shops);
    }
}

