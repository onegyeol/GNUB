package pbl.GNUB.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import pbl.GNUB.entity.College;
import pbl.GNUB.entity.Department;
import pbl.GNUB.repository.DepartmentRepository;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Transactional
    public Department saveDepartment(String name, College college) { // 해당 단과대에 속하는 학과이름 저장
        Department department = new Department(name, college);
        return departmentRepository.save(department); // 학과이름 저장
    }

    public List<Department> getDepartmentsByCollege(College college) {
        return departmentRepository.findByCollege(college);
    }
    
    // 영어 -> 한글 변환
    public String getCollegeKoreanName(College college) {
        return college.getKoreanName();
    }
    
}
