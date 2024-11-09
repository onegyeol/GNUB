package pbl.GNUB.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import pbl.GNUB.entity.Department;
import pbl.GNUB.entity.Member;
import pbl.GNUB.repository.DepartmentRepository;
import pbl.GNUB.repository.MemberRepository;
import pbl.GNUB.dto.MemberFormDto;

@Service
@Transactional // 모든 메서드에서 트랜잭션을 지원합니다.
@RequiredArgsConstructor // final 필드에 대한 생성자를 자동으로 생성합니다.
public class MemberService {
    private final MemberRepository memberRepository;
    private final DepartmentRepository departmentRepository;

    // 회원가입
    public Long save(MemberFormDto memberFormDto) {
        // 이메일 중복 체크
        if (isEmailDuplicate(memberFormDto.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다: " + memberFormDto.getEmail());
        }
        // 1. dto -> entity 변환
        Member member = new Member();
        member.setName(memberFormDto.getName());
        member.setEmail(memberFormDto.getEmail());
        member.setPassword(memberFormDto.getPassword());

        // 비밀번호 암호화
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(memberFormDto.getPassword());
        member.setPassword(encodedPassword);

        // 2. departmentId로부터 Department 객체 찾기
        Department department = departmentRepository.findById(memberFormDto.getDepartmentId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 학과입니다."+ memberFormDto.getDepartmentId()));
        member.setDepartment(department); // Department 객체 설정

        // 3. repository의 save 메서드 호출
        memberRepository.save(member);
        return member.getId();
    }

    // 이메일 중복 여부 확인 메소드
    public Boolean isEmailDuplicate(String email) {
        return memberRepository.findByEmail(email).isPresent();
    }
 
    public MemberFormDto login(MemberFormDto memberFormDto){
        // 이메일 입력 확인
        if (memberFormDto.getEmail() == null || memberFormDto.getEmail().isEmpty()) {
            throw new IllegalArgumentException("이메일을 입력하세요.");
        }

        // 1. 회원이 입력한 이메일로 DB에서 조회
        Optional<Member> byEmail = memberRepository.findByEmail(memberFormDto.getEmail());
        if(byEmail.isPresent()) {
            // 조회 결과가 있다(해당 이메일을 가진 회원 정보가 있다)
            Member member = byEmail.get();

            // 2. DB에서 조회한 비밀번호와 사용자가 입력한 비밀번호가 일치하는지 판단
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            if (passwordEncoder.matches(memberFormDto.getPassword(), member.getPassword())) {
                // 비밀번호가 일치
                // entity -> dto 변환
                MemberFormDto dto = MemberFormDto.toMemberFormDto(member);
                return dto;

            } else{
                // 비밀번호 불일치(로그인 실패)
                System.out.println("비밀번호 불일치");
                return null;
            }
        
        } else{
            // 조회 결과가 없다(해당 이메일을 가진 회원이 없다.)
            System.out.println("조회 결과 없음");
            return null;
        }
    }

    public MemberFormDto getMemberInfoByEmail(String email) {
        // Optional을 사용하여 회원을 조회
        Member member = memberRepository.findByEmail(email)
                                        .orElseThrow(() -> new IllegalArgumentException("해당 이메일로 회원을 찾을 수 없습니다: " + email));
    
        // Member 엔티티를 MemberFormDto로 변환
        return MemberFormDto.toMemberFormDto(member);
    }

    public Department getDepartmentById(Long departmentId) {
        return departmentRepository.findById(departmentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 학과입니다: " + departmentId));
    }
    
    

}