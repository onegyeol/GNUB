package pbl.GNUB.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberInfoResponseDto {
    private String name;
    private String email;
    private String departmentName;
    private String departmentCollege;
}
