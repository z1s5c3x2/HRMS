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

    private int tm_no;        // 프로젝트 팀원 번호
    private int emp_no;            // 사원번호 (fk)
    private String tm_st;     // 투입 날짜
    private String tm_end;    // 투입 종료 날짜

    private int pjt_no;         // 프로젝트 번호(fk)
    private int aprv_no;            // 결재번호(fk)

    // entity 저장할때 메소드
    public TeamMemberEntity saveToEntity(){
        return TeamMemberEntity.builder()
                .tm_st(this.tm_st)
                .build();
    }
}
