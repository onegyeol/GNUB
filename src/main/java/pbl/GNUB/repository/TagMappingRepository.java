package pbl.GNUB.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import pbl.GNUB.entity.TagMapping;

@Repository
public interface TagMappingRepository extends JpaRepository<TagMapping, Long> {
    @Query("SELECT t.englishName FROM TagMapping t WHERE t.koreanName = :koreanTag")
    Optional<String> findEnglishNameByKoreanName(String koreanTag);
}
