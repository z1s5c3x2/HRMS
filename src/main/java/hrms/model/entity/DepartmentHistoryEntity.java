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
    private int htrdpNo;       // 부서이동 히스토리 넘버
    private int htrdpRk;        // 권한레벨(직급)
    @Column
    private String hdptmStart;
    private String hdptmEnd;
    @ToString.Exclude
    @JoinColumn(name="dptmNo")
    @ManyToOne
    private DepartmentEntity dptmNo;
    @ToString.Exclude
    @JoinColumn(name="empNo")
    @ManyToOne
    private EmployeeEntity empNo;
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name="aprvNo")
    private ApprovalEntity aprvNo;


}
