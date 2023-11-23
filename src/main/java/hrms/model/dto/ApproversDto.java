package hrms.model.dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ApproversDto {

    private String empName;        // 검토자명
    private int empRk;             // 검토자 직급
    private String dptmName;       // 검토자 부서명
    private int apState;           // 결재완료 여부 ( 1:완료 / 2:반려 / 3:검토중 )
    private LocalDateTime udate;   // 상신일

}
