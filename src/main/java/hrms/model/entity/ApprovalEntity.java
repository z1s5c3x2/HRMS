package hrms.model.entity;

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
    private int aprv_no;            // 결재번호
    @Column(nullable = false)
    private int aprv_type;          // 결재타입 (1 프로젝트팀결성 / 2 프로젝트기획 / 3 연차,휴직,병가 / 4 퇴사)
    @ColumnDefault( "''" )          // default
    @Column( length = 40 )
    private String aprv_cont;       // 간단한 결재내용
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name="emp_no")
    private EmployeeEntity emp_no;  // 상신자

    @Builder.Default
    @OneToMany(mappedBy="aprv_no")
    private List<ApprovalLogEntity> approvalLogEntities = new ArrayList<>();
    @Builder.Default
    @OneToMany(mappedBy="aprv_no")
    private List<LeaveRequestEntity> leaveRequestEntities = new ArrayList<>();
    @Builder.Default
    @OneToMany(mappedBy = "aprv_no")
    private List<ProjectEntity> projectEntities = new ArrayList<>();
    @Builder.Default
    @OneToMany(mappedBy = "aprv_no")
    private List<TeamMemberEntity> teamMemberEntities = new ArrayList<>();
    @Builder.Default
    @OneToMany(mappedBy = "aprv_no")
    private List<DepartmentHistoryEntity> departmentHistoryEntities = new ArrayList<>();
    @Builder.Default
    @OneToMany(mappedBy = "aprv_no")
    private List<EmployeeEntity> employeeEntities = new ArrayList<>();
    @Builder.Default
    @OneToMany(mappedBy = "aprv_no")
    private List<RetiredEmployeeEntity> retiredEmployees = new ArrayList<>();
    @Builder.Default
    @OneToMany(mappedBy = "aprv_no")
    private List<SalaryEntity> salaryEntities = new ArrayList<>();

}
