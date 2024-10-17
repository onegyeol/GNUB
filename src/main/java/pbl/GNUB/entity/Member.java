package pbl.GNUB.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pbl.GNUB.dto.MemberFormDto;

// 회원 정보 저장
@Entity
@Table(name = "MEMBER")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Member {
    @Id @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id; // 회원 고유 식별자

    private String name; // 회원 이름

    @Column(unique = true)
    private String email; // 회원 이메일

    private String password; // 회원 비밀번호 (암호화됨)

    @ManyToOne(fetch = FetchType.LAZY) //지연로딩
    @JoinColumn(name = "DEPARTMENT_ID") 
    private Department department; // 학과

    // MemberFormDto를 기반으로 Member 객체를 생성하는 메서드
    public static Member toMember(MemberFormDto memberFormDto, Department department) {
        Member member = new Member();
        member.setName(memberFormDto.getName());
        member.setPassword(memberFormDto.getPassword());
        member.setEmail(memberFormDto.getEmail());
        member.setDepartment(department); // Department 객체 설정
        return member;
    }
}
