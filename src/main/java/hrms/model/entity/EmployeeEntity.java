package hrms.model.entity;



import hrms.model.dto.EmployeeDto;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter @Builder @ToString @AllArgsConstructor @NoArgsConstructor
@Entity @Table(name="EMP")

public class EmployeeEntity extends BaseTime {
    @Id
    private int emp_no;                     // 사원번호
    @Column( )
    private String empName;                // 사원이름
    @Column( )
    private String emp_phone;               // 사원전환번호
    @Column( )
    private String emp_pwd;                 // 사원비밀번호
    @Column( )
    private String emp_sex;                 // 사원성별
    @Column( )
    private String emp_acn;                 // 사원계좌번호
    @Column()
    private boolean empSta;                 // 근무상태
    @Column()
    private int emp_rk;                     // 사원 직급

    @ToString.Exclude
    @JoinColumn(name="dptm_no")
    @ManyToOne
    private DepartmentEntity dptm_no;
    @ToString.Exclude
    @JoinColumn(name="aprv_no")
    @ManyToOne
    private ApprovalEntity aprv_no;

    @Builder.Default
    @OneToMany(mappedBy = "emp_no")         // 결재테이블연결
    private List<ApprovalEntity> approval_entities = new ArrayList<>();
    @Builder.Default
    @OneToMany(mappedBy = "emp_no")
    private List<ApprovalLogEntity> approval_logs = new ArrayList<>();
    @Builder.Default
    @OneToMany(mappedBy = "emp_no")
    private List<ProjectEntity> project_entities = new ArrayList<>();
    @Builder.Default
    @OneToMany(mappedBy = "emp_no")
    private List<AttendanceEntity> attendanceEntities = new ArrayList<>();
    @Builder.Default
    @OneToMany(mappedBy = "emp_no")
    private List<DepartmentHistoryEntity> department_entities = new ArrayList<>();
    @Builder.Default
    @OneToMany(mappedBy = "emp_no")
    private List<LeaveRequestEntity> leaveRequestEntities = new ArrayList<>();
    @Builder.Default
    @OneToMany(mappedBy = "emp_no")
    private List<RetiredEmployeeEntity> retiredEmployeeEntities = new ArrayList<>();
    @Builder.Default
    @OneToMany(mappedBy = "emp_no")
    private List<SalaryEntity> salaryEntities = new ArrayList<>();
    @Builder.Default
    @OneToMany(mappedBy = "emp_no")
    private List<TeamMemberEntity> teamMemberEntities = new ArrayList<>();


    public EmployeeDto allToDto()
    {
        return EmployeeDto.builder()
                .emp_no(this.emp_no)
                .emp_name(this.empName)
                .emp_phone(this.emp_phone)
                .emp_pwd(this.emp_pwd)
                .emp_sex(this.emp_sex)
                .emp_acn(this.emp_acn)
                .emp_sta(this.empSta)
                .emp_rk(this.emp_rk).build();
    }

}
