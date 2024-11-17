package pbl.GNUB.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pbl.GNUB.entity.ShopTag;

public interface ShopTagRepository extends JpaRepository<ShopTag, Long> {
    ShopTag findByName(String name);
    List<ShopTag> findByValue(Integer value);

    @Query("SELECT s FROM ShopTag s WHERE s.name = :name " +
            "AND s.hygiene = :hygiene AND s.revisit = :revisit " +
            "AND s.recent = :recent AND s.delicious = :delicious " +
            "AND s.goodValue = :goodValue AND s.mood = :mood " +
            "AND s.fresh = :fresh AND s.kindness = :kindness " +
            "AND s.alone = :alone AND s.chilamDong = :chilamDong " +
            "AND s.gajwaDong = :gajwaDong")
    ShopTag findByAllFields(@Param("name") String name,
                            @Param("hygiene") int hygiene,
                            @Param("revisit") int revisit,
                            @Param("recent") int recent,
                            @Param("delicious") int delicious,
                            @Param("goodValue") int goodValue,
                            @Param("mood") int mood,
                            @Param("fresh") int fresh,
                            @Param("kindness") int kindness,
                            @Param("alone") int alone,
                            @Param("chilamDong") int chilamDong,
                            @Param("gajwaDong") int gajwaDong);
                        }
    


