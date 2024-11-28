package pbl.GNUB.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pbl.GNUB.entity.Data;

@Repository
public interface DataRepository extends JpaRepository<Data, Long> {
}
