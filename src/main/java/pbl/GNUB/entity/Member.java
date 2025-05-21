package pbl.GNUB.entity;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import pbl.GNUB.dto.MemberFormDto;

// 회원 정보 저장
@Entity
@Table(name = "MEMBER")
@Getter @Setter
@NoArgsConstructor  // 기본 생성자 추가
@ToString
public class Member implements UserDetails {

    @Id @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id; // 회원 고유 식별자

    private String name; // 회원 이름

    @Column(unique = true)
    @Email
    private String email; // 회원 이메일

    private String password; // 회원 비밀번호 (암호화됨)

    @ManyToOne(fetch = FetchType.LAZY) // 지연 로딩
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 기본적으로 ROLE_USER 권한을 부여
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return this.password;  // UserDetails에서 요구하는 메서드
    }

    @Override
    public String getUsername() {
        return this.email;  // 여기서는 이메일을 유저네임으로 사용
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
