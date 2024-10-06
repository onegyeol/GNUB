package pbl.GNUB.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pbl.GNUB.entity.Shop;

public interface ShopRepository extends JpaRepository<Shop, Long>{
    
}
