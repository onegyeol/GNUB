package pbl.GNUB.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import pbl.GNUB.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long>{
    Optional<Member> findByEmail(String email); //이메일로 중복여부 판단
}
