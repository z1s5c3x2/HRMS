package hrms.model.dto;

import lombok.*;

@Getter @Setter  @Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeSearchOptionDto {
    private int page; // 현재 페이지
    private String searchNameOrEmpNo; // 이름 또는 사번 검색
    private String searchValue; // 검색 문자열
}
