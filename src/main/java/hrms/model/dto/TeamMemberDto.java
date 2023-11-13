package hrms.model.dto;

import hrms.model.entity.ApprovalEntity;
import hrms.model.entity.EmployeeEntity;
import hrms.model.entity.ProjectEntity;
import hrms.model.entity.TeamMemberEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@AllArgsConstructor@NoArgsConstructor
@Getter@Setter@ToString
@Builder
public class TeamMemberDto {

    private int tmNo;        // 프로젝트 팀원 번호
    private String empName;     // 사원 이름
    private String empNo;       // 사원번호 (fk)
    private LocalDate tmSt;     // 투입 날짜
    private LocalDate tmEnd;    // 투입 종료 날짜

    private int pjtNo;         // 프로젝트 번호(fk)
    private String pjtName;     // 프로젝트명
    private int aprvNo;            // 결재번호(fk)
    private int aprvSta;     // 결재상태(1:승인,2:반려,3:검토중)

    // entity 저장할때 메소드
    public TeamMemberEntity saveToEntity(){
        return TeamMemberEntity.builder()
                .tmSt(this.tmSt.plusDays(1))
                .build();
    }

}
