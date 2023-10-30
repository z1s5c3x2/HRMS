package hrms.model.entity;


import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="HTRDP")
public class DepartmentHistoryEntity extends BaseTime {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int htrdp_no;       // 부서이동 히스토리 넘버
    private int htrdp_rk;    // 권한레벨(직급)
    @ToString.Exclude
    @JoinColumn(name="dptm_no")
    @ManyToOne
    private DepartmentEntity dptm_no;
    @ToString.Exclude
    @JoinColumn(name="emp_no")
    @ManyToOne
    private EmployeeEntity emp_no;
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name="aprv_no")
    private ApprovalEntity aprv_no;


}
