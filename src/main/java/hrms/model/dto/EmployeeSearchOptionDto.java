package hrms.model.dto;

import lombok.*;

@Getter @Setter  @Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeSearchOptionDto {
    private String searchOptionNameOrEmpNo; // 이름 또는 사번 검색
    private String searchOptionSta;         // 사원 상태
    private String searchOptionSearchValue; // 검색 문자열
}
