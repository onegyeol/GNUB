package pbl.GNUB.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pbl.GNUB.dto.ShopTagDto;
import pbl.GNUB.entity.ShopTag;
import pbl.GNUB.repository.ShopTagRepository;

@Service
public class ShopTagService {

    @Autowired
    ShopTagRepository shopTagRepository;

    public List<ShopTagDto> getShopsByTag(String tag, int value) {
        List<ShopTag> shopTags;

        switch (tag) {
            case "alone":
                shopTags = shopTagRepository.findByAlone(value);
                break;
            case "date":
                shopTags = shopTagRepository.findByDate(value);
                break;
            case "delicious":
                shopTags = shopTagRepository.findByDelicious(value);
                break;
            case "fresh":
                shopTags = shopTagRepository.findByFresh(value);
                break;
            case "goodValue":
                shopTags = shopTagRepository.findByGoodValue(value);
                break;
            case "hygiene":
                shopTags = shopTagRepository.findByHygiene(value);
                break;
            case "kindness":
                shopTags = shopTagRepository.findByKindness(value);
                break;
            case "many":
                shopTags = shopTagRepository.findByMany(value);
                break;
            case "mood":
                shopTags = shopTagRepository.findByMood(value);
                break;
            case "parking":
                shopTags = shopTagRepository.findByParking(value);
                break;
            case "recent":
                shopTags = shopTagRepository.findByRecent(value);
                break;
            default:
                throw new IllegalArgumentException("Invalid tag: " + tag);
        }

        return shopTags.stream()
            .map(ShopTagDto::new)
            .collect(Collectors.toList());
    }
}
