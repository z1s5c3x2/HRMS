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
    private int tmNo;        // 프로젝트 팀원 번호
    @ToString.Exclude
    @JoinColumn(name="empNo")
    @ManyToOne
    private EmployeeEntity empNo;            // 사원번호 (fk)
    @Column
    private String tmSt;     // 투입 날짜
    @Column
    private String tmEnd;    // 투입 종료 날짜

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name="pjtNo")
    private ProjectEntity pjtNo;         // 프로젝트 번호(fk)
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name="aprvNo")
    private ApprovalEntity aprvNo;            // 결재번호(fk)

}
