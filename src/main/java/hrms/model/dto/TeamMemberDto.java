package hrms.model.dto;

import hrms.model.entity.ApprovalEntity;
import hrms.model.entity.EmployeeEntity;
import hrms.model.entity.ProjectEntity;
import hrms.model.entity.TeamMemberEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@AllArgsConstructor@NoArgsConstructor
@Getter@Setter@ToString
@Builder
public class TeamMemberDto {

    private int tmNo;        // 프로젝트 팀원 번호
    private int empNo;            // 사원번호 (fk)
    private String tmSt;     // 투입 날짜
    private String tmEnd;    // 투입 종료 날짜

    private int pjtNo;         // 프로젝트 번호(fk)
    private int aprvNo;            // 결재번호(fk)

    // entity 저장할때 메소드
    public TeamMemberEntity saveToEntity(){
        return TeamMemberEntity.builder()
                .tmSt(this.tmSt)
                .build();
    }
}
