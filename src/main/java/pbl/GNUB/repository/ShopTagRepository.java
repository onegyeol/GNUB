package pbl.GNUB.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pbl.GNUB.entity.ShopTag;

public interface ShopTagRepository extends JpaRepository<ShopTag, Long> {
        ShopTag findByName(String name);

        @Query("SELECT s FROM ShopTag s WHERE s.name = :name " +
            "AND s.alone = :alone AND s.date = :date " +
            "AND s.delicious = :delicious AND s.fresh = :fresh " +
            "AND s.goodValue = :goodValue AND s.hygiene = :hygiene " +
            "AND s.kindness = :kindness AND s.many = :many " +
            "AND s.mood = :mood AND s.parking = :parking " +
            "AND s.recent = :recent")
        ShopTag findByAllFields(@Param("name") String name,
                            @Param("alone") int alone,
                            @Param("date") int date,
                            @Param("delicious") int delicious,
                            @Param("fresh") int fresh,
                            @Param("goodValue") int goodValue,
                            @Param("hygiene") int hygiene,
                            @Param("kindness") int kindness,
                            @Param("many") int many,
                            @Param("mood") int mood,
                            @Param("parking") int parking,
                            @Param("recent") int recent);
                            
        List<ShopTag> findByAlone(int value);
        List<ShopTag> findByDate(int value);
        List<ShopTag> findByDelicious(int value);
        List<ShopTag> findByFresh(int value);
        List<ShopTag> findByGoodValue(int value);
        List<ShopTag> findByHygiene(int value);
        List<ShopTag> findByKindness(int value);
        List<ShopTag> findByMany(int value);
        List<ShopTag> findByMood(int value);
        List<ShopTag> findByParking(int value);
        List<ShopTag> findByRecent(int value);
}
    

                    