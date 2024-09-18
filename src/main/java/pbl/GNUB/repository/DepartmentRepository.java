package pbl.GNUB.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pbl.GNUB.entity.College;
import pbl.GNUB.entity.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long>{
    List<Department> findByCollege(College college);

}
