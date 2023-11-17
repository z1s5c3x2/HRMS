package hrms.model.dto;

import hrms.model.entity.ApprovalEntity;
import hrms.model.entity.EmployeeEntity;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ApprovalDto {

    private int aprvNo;            // 결재번호
    private int aprvType;          // 결재타입 (1 프로젝트팀결성 / 2 프로젝트기획 / 3 연차,휴직,병가 / 4 퇴사)
    private String aprvCont;       // 간단한 결재내용
    private String aprvJson;       // json형식의 문자열[테이블별 변경할 객체 정보 저장]
    private String empNo;             // 상신자
    private LocalDateTime cdate;   // 상신일
    private int apState;           // 결재완료 여부( 1:완료 / 2:반려 / 3:검토중 )
    private String empName;         // 상신자명

    public void setAprvNo(int aprvNo) {
        this.aprvNo = aprvNo;
    }
}
