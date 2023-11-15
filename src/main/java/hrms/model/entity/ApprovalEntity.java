package hrms.model.entity;

import hrms.model.dto.ApprovalDto;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Table( name = "APRV" )
// 결재테이블
public class ApprovalEntity extends BaseTime {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private int aprvNo;            // 결재번호
    @Column(nullable = false)
    private int aprvType;          // 결재타입 (1 프로젝트팀결성 / 2 프로젝트기획 / 3 연차,휴직,병가 / 4 퇴사)
    @ColumnDefault( "''" )          // default
    @Column( length = 40 )
    private String aprvCont;       // 간단한 결재내용
    @Column(columnDefinition = "LONGTEXT")
    private String aprvJson;       // json형식의 문자열[테이블별 변경할 객체 정보 저장]
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name="empNo")
    private EmployeeEntity empNo;  // 상신자


    @Builder.Default
    @OneToMany( fetch = FetchType.LAZY  , cascade = CascadeType.ALL ,mappedBy="aprvNo")
    private List<ApprovalLogEntity> approvalLogEntities = new ArrayList<>();


    @Builder.Default
    @OneToMany( fetch = FetchType.LAZY  , cascade = CascadeType.ALL ,mappedBy="aprvNo")
    private List<LeaveRequestEntity> leaveRequestEntities = new ArrayList<>();
    @Builder.Default
    @OneToMany( fetch = FetchType.LAZY  , cascade = CascadeType.ALL ,mappedBy = "aprvNo")
    private List<ProjectEntity> projectEntities = new ArrayList<>();
    @Builder.Default
    @OneToMany( fetch = FetchType.LAZY  , cascade = CascadeType.ALL ,mappedBy = "aprvNo")
    private List<TeamMemberEntity> teamMemberEntities = new ArrayList<>();
    @Builder.Default
    @OneToMany( fetch = FetchType.LAZY  , cascade = CascadeType.ALL ,mappedBy = "aprvNo")
    private List<DepartmentHistoryEntity> departmentHistoryEntities = new ArrayList<>();
    @Builder.Default
    @OneToMany( fetch = FetchType.LAZY  , cascade = CascadeType.ALL ,mappedBy = "aprvNo")
    private List<RetiredEmployeeEntity> retiredEmployees = new ArrayList<>();
    @Builder.Default
    @OneToMany( fetch = FetchType.LAZY  , cascade = CascadeType.ALL , mappedBy = "aprvNo")
    private List<SalaryEntity> salaryEntities = new ArrayList<>();


    public ApprovalDto toApprovalDto() {

        return ApprovalDto.builder()
                .aprvNo( this.aprvNo )           // 결재번호
                .aprvType( this.aprvType )       // 결재타입 (1 프로젝트팀결성 / 2 프로젝트기획 / 3 연차,휴직,병가 / 4 퇴사)
                .aprvCont( this.aprvCont )       // 간단한 결재내용
                .aprvJson( this.aprvJson )       // json형식의 문자열[테이블별 변경할 객체 정보 저장]
                .empNo( this.empNo.getEmpNo() )  // 상신자
                .cdate( this.cdate )             // 상신일
                .apState( 0 )                    // 결재완료 여부( 1:완료 / 2:검토중 / 3:반려 )
                .build();
    }

}
