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
    private int empNo;                     // 사원번호
    @Column( )
    private String empName;                // 사원이름
    @Column( )
    private String empPhone;               // 사원전환번호
    @Column( )
    private String empPwd;                 // 사원비밀번호
    @Column( )
    private String empSex;                 // 사원성별
    @Column( )
    private String empAcn;                 // 사원계좌번호
    @Column()
    private boolean empSta;                 // 근무상태
    @Column()
    private int empRk;                     // 사원 직급

    @ToString.Exclude
    @JoinColumn(name="dptm_no")
    @ManyToOne
    private DepartmentEntity dptmNo;
    @ToString.Exclude
    @JoinColumn(name="aprv_no")
    @ManyToOne
    private ApprovalEntity aprvNo;

    @Builder.Default
    @OneToMany(mappedBy = "empNo")         // 결재테이블연결
    private List<ApprovalEntity> approvalEntities = new ArrayList<>();
    @Builder.Default
    @OneToMany(mappedBy = "empNo")
    private List<ApprovalLogEntity> approvalLogs = new ArrayList<>();
    @Builder.Default
    @OneToMany(mappedBy = "empNo")
    private List<ProjectEntity> projectEntities = new ArrayList<>();
    @Builder.Default
    @OneToMany(mappedBy = "empNo")
    private List<AttendanceEntity> attendanceEntities = new ArrayList<>();
    @Builder.Default
    @OneToMany(mappedBy = "empNo")
    private List<DepartmentHistoryEntity> departmentEntities = new ArrayList<>();
    @Builder.Default
    @OneToMany(mappedBy = "empNo")
    private List<LeaveRequestEntity> leaveRequestEntities = new ArrayList<>();
    @Builder.Default
    @OneToMany(mappedBy = "empNo")
    private List<RetiredEmployeeEntity> retiredEmployeeEntities = new ArrayList<>();
    @Builder.Default
    @OneToMany(mappedBy = "empNo")
    private List<SalaryEntity> salaryEntities = new ArrayList<>();
    @Builder.Default
    @OneToMany(mappedBy = "empNo")
    private List<TeamMemberEntity> teamMemberEntities = new ArrayList<>();


    public EmployeeDto allToDto()
    {
        return EmployeeDto.builder()
                .empNo(this.empNo)
                .empName(this.empName)
                .empPhone(this.empPhone)
                .empPwd(this.empPwd)
                .empSex(this.empSex)
                .empAcn(this.empAcn)
                .empSta(this.empSta)
                .dtpmNo(this.dptmNo.getDptmNo())
                .empRk(this.empRk).build();
    }

}
