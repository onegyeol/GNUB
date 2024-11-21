package pbl.GNUB.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pbl.GNUB.repository.TagMappingRepository;

@Service
public class TagMappingService {

    @Autowired
    private TagMappingRepository tagMappingRepository;

    public String toEnglish(String koreanTag) {
        return tagMappingRepository.findEnglishNameByKoreanName(koreanTag)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Korean tag: " + koreanTag));
    }
}
