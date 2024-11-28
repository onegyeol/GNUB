package pbl.GNUB.service;

import org.springframework.stereotype.Service;

import pbl.GNUB.dto.DataDto;
import pbl.GNUB.entity.Data;
import pbl.GNUB.entity.Shop;
import pbl.GNUB.entity.ShopTag;
import pbl.GNUB.repository.DataRepository;
import pbl.GNUB.repository.ShopRepository;
import pbl.GNUB.repository.ShopTagRepository;

import java.util.List;


@Service
public class DataService {

    private final ShopRepository shopRepository;
    private final ShopTagRepository shopTagRepository;
    private final DataRepository dataRepository;

    public DataService(ShopRepository shopRepository, ShopTagRepository shopTagRepository,DataRepository dataRepository) {
        this.shopRepository = shopRepository;
        this.shopTagRepository = shopTagRepository;
        this.dataRepository = dataRepository;
    }

    // Shop 데이터 저장
    public Shop saveShop(Shop shop) {
        return shopRepository.save(shop);
    }

    // ShopTag 데이터 저장
    public ShopTag saveShopTag(ShopTag shopTag) {
        return shopTagRepository.save(shopTag);
    }

    // 사용자 질문, gpt 답변 저장
    public void saveUserInput(DataDto dataDto) {
        Data data = new Data();
        data.setUserInput(dataDto.getUserInput());
        data.setGptResponse(dataDto.getGptResponse());
        dataRepository.save(data);
    }

    // 모든 레스토랑 데이터를 반환
    public List<Shop> getAllRestaurants() {
        return shopRepository.findAll();
    }
}

    

