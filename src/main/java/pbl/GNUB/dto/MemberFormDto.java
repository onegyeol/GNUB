package pbl.GNUB.dto;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pbl.GNUB.entity.Department;
import pbl.GNUB.entity.Member;

@Getter @Setter
@ToString
@NoArgsConstructor
public class MemberFormDto {

    @NotBlank(message = "이름 입력은 필수값입니다.")
    private String name;

    @NotEmpty(message = "이메일 입력은 필수값입니다.")
    @Email(message = "이메일 형식으로 입력해주세요.")
    private String email;

    @NotEmpty(message = "비밀번호 입력은 필수값입니다.")
    @Length(min = 8, max = 16, message = "비밀번호는 8자 이상, 16자 이하로 입력해주세요.")
    private String password;

    @NotEmpty(message = "단과대학 입력은 필수값입니다.") // 추가: 필수값 체크
    private Long departmentId;
 
    public static MemberFormDto toMemberFormDto(Member member){
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setEmail(member.getEmail());
        memberFormDto.setName(member.getName());
        memberFormDto.setPassword(member.getPassword());

        if (member.getDepartment() != null) {
            memberFormDto.setDepartmentId(member.getDepartment().getId());  // department의 ID를 가져옴
        }
        
        return memberFormDto;
    }
}
