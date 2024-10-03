package pbl.GNUB.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// 회원 정보 저장
@Entity
@Table(name = "MEMBER")
@Getter @Setter
@ToString
public class Member {
    @Id @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id; // 회원 고유 식별자

    private String name; // 회원 이름

    @Column(unique = true)
    private String email; // 회원 이메일

    private String password; // 회원 비밀번호 (암호화됨)

    @ManyToOne
    @JoinColumn(name = "DEPARTMENT_ID") 
    private Department department; // 학과
}
