package hrms.model.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor@NoArgsConstructor
@Getter@Setter
@Builder@ToString
@Table(name = "TM_MNG")
public class TeamMemberEntity extends BaseTime{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int tm_no;        // 프로젝트 팀원 번호
    @ToString.Exclude
    @JoinColumn(name="emp_no")
    @ManyToOne
    private EmployeeEntity emp_no;            // 사원번호 (fk)
    @Column
    private String tm_st;     // 투입 날짜
    @Column
    private String tm_end = null;    // 투입 종료 날짜

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name="pjt_no")
    private ProjectEntity pjt_no;         // 프로젝트 번호(fk)
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name="aprv_no")
    private ApprovalEntity aprv_no;            // 결재번호(fk)

}
