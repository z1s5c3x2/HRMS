package hrms.model.entity;



import hrms.model.dto.EmployeeDto;
import hrms.model.dto.EmployeeSearchDto;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter @Builder @ToString @AllArgsConstructor @NoArgsConstructor
@Entity @Table(name="EMP")

public class EmployeeEntity extends BaseTime {
    @Id
    private String empNo;                     // 사원번호
    @Column( )
    private String empName;                // 사원이름
    @Column( )
    private String empPhone;               // 사원전화번호
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
    @JoinColumn(name="dptmNo")
    @ManyToOne
    private DepartmentEntity dptmNo;

    @Builder.Default
    @OneToMany( fetch = FetchType.LAZY  , cascade = CascadeType.ALL ,mappedBy = "empNo")         // 결재테이블연결
    private List<ApprovalEntity> approvalEntities = new ArrayList<>();
    @Builder.Default
    @OneToMany( fetch = FetchType.LAZY  , cascade = CascadeType.ALL ,mappedBy = "empNo")
    private List<ApprovalLogEntity> approvalLogs = new ArrayList<>();
    @Builder.Default
    @OneToMany( fetch = FetchType.LAZY  , cascade = CascadeType.ALL ,mappedBy = "empNo")
    private List<ProjectEntity> projectEntities = new ArrayList<>();
    @Builder.Default
    @OneToMany( fetch = FetchType.LAZY  , cascade = CascadeType.ALL ,mappedBy = "empNo")
    private List<AttendanceEntity> attendanceEntities = new ArrayList<>();
    @Builder.Default
    @OneToMany( fetch = FetchType.LAZY  , cascade = CascadeType.ALL ,mappedBy = "empNo")
    private List<DepartmentHistoryEntity> departmentHistoryEntities = new ArrayList<>();
    @Builder.Default
    @OneToMany( fetch = FetchType.LAZY  , cascade = CascadeType.ALL ,mappedBy = "empNo")
    private List<LeaveRequestEntity> leaveRequestEntities = new ArrayList<>();
    @Builder.Default
    @OneToMany( fetch = FetchType.LAZY  , cascade = CascadeType.ALL ,mappedBy = "empNo")
    private List<RetiredEmployeeEntity> retiredEmployeeEntities = new ArrayList<>();
    @Builder.Default
    @OneToMany( fetch = FetchType.LAZY  , cascade = CascadeType.ALL ,mappedBy = "empNo")
    private List<SalaryEntity> salaryEntities = new ArrayList<>();
    @Builder.Default
    @OneToMany( fetch = FetchType.LAZY  , cascade = CascadeType.ALL ,mappedBy = "empNo")
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
                .dptmNo(this.dptmNo.getDptmNo())
                .empRk(this.empRk).build();
    }
    public EmployeeDto notPwdToDto()
    {
        return EmployeeDto.builder()
                .empNo(this.empNo)
                .empName(this.empName)
                .empPhone(this.empPhone)
                .empSex(this.empSex)
                .empAcn(this.empAcn)
                .empSta(this.empSta)
                .dptmNo(this.dptmNo.getDptmNo())
                .empRk(this.empRk).build();
    }
    public EmployeeDto searchToDto()
    {
        return EmployeeDto.builder()
                .empNo(this.empNo)
                .empName(this.empName)
                .empPhone(this.empPhone)
                .empSta(this.empSta)
                .dptmNo(this.dptmNo.getDptmNo())
                .dptmName(this.dptmNo.getDptmName())
                .empRk(this.empRk).build();
    }

    public EmployeeSearchDto searchInfoToDto()
    {
        return EmployeeSearchDto.builder()
                .empName(this.empName)
                .empNo(this.empNo)
                .empSex(this.empSex)
                .empAcn(this.empAcn)
                .empSta(this.empSta)
                .empRk(this.empRk)
                .dptmName(this.dptmNo.getDptmName())
                .build();
    }

}
