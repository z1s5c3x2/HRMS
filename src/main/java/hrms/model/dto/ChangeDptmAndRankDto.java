package hrms.model.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ChangeDptmAndRankDto {
    private String empNo; // 사원 번호
    private int infoData; //변경할 직급 or 부서
    private String changeDate; // 변경할 날짜


}
