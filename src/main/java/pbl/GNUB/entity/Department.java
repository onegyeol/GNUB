package pbl.GNUB.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "DEPARTMENT")
@Getter @Setter
public class Department {

    @Id @GeneratedValue
    @Column(name = "DEPARTMENT_ID")
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private College college; // 단과대학 정보

    public Department() {} //매개변수 없는 생성자

    public Department(String name, College college) { // 매개변수 있는 생성자
        this.name = name;
        this.college = college;
    }
}
