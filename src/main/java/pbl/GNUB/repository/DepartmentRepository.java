package pbl.GNUB.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import pbl.GNUB.entity.College;
import pbl.GNUB.entity.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long>{
    List<Department> findByCollege(College college); // 단과대 이름으로 해당 학과 찾기위한 메소드

    Optional<Department> findByName(Department department); // 이름으로 학과 찾기 위한 메소드 추가
}
