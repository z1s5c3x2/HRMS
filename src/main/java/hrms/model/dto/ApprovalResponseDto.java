package hrms.model.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ApprovalResponseDto {

    private int aprvNo;            // 결재번호
    private int aprvType;          // 결재타입 (1 프로젝트팀결성 / 2 프로젝트기획 / 3 연차,휴직,병가 / 4 퇴사)
    private String aprvCont;       // 간단한 결재내용
    private String empName;        // 상신자명
    private int apState;           // 결재완료 여부 ( 1:완료 / 2:반려 / 3:검토중 )
    private LocalDateTime cdate;   // 상신일
    private int empRk;             // 사원 직급
    private String dptmName;       // 부서 이름
    private List<ApproversDto> approvers;    // 다수의 검토자명

}
