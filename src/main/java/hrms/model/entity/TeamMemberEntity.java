package hrms.model.entity;

import hrms.model.dto.TeamMemberDto;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@AllArgsConstructor@NoArgsConstructor
@Getter@Setter
@Builder@ToString
@Table(name = "TM_MNG")
public class TeamMemberEntity extends BaseTime{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int tmNo;        // 프로젝트 팀원 번호
    @ToString.Exclude
    @JoinColumn(name="empNo")
    @ManyToOne
    private EmployeeEntity empNo;            // 사원번호 (fk)
    @Column
    private LocalDate tmSt;     // 투입 날짜

    @Column
    private LocalDate tmEnd;    // 투입 종료 날짜

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name="pjtNo")
    private ProjectEntity pjtNo;         // 프로젝트 번호(fk)
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name="aprvNo")
    private ApprovalEntity aprvNo;            // 결재번호(fk)


    // 1. 팀원 개별 조회시 메소드
    public TeamMemberDto oneToDto(){
        return TeamMemberDto.builder()
                .tmNo(this.tmNo)
                .empNo(this.empNo.getEmpNo())
                .empName(this.empNo.getEmpName())
                .tmSt(this.tmSt)
                .tmEnd(this.tmEnd)
                .pjtNo(this.pjtNo.getPjtNo())
                .pjtName(this.pjtNo.getPjtName())
                .aprvNo(this.aprvNo.getAprvNo())
                .aprvSta(settingAprvNo(this.aprvNo))
                .build();
    }

    // 결재상태 설정 메소드
    public int settingAprvNo(ApprovalEntity aprvNo){

        List<ApprovalLogEntity> approvalLogEntities = aprvNo.getApprovalLogEntities();
        boolean allStaThree = true;     // 검토중 판단
        boolean hasRejection = false;   // 반려상태 판단

        for (ApprovalLogEntity approvalLogEntity : approvalLogEntities) {
            int aplogSta = approvalLogEntity.getAplogSta();

            if (aplogSta == 2) {
                hasRejection = true;
            } else if (aplogSta != 1) {
                allStaThree = false;
            }
        }

        if (allStaThree) {
            // 모두 1(승인) 상태일 때 승인
            return 1;
        } else if (hasRejection) {
            // 2(반려) 상태가 하나라도 있을 때 반려
            return 2;
        } else{
            // 나머지 경우의 수, 3(검토중) 상태가 있을 때 검토중
            return 3;
        }
    } // method end



}
