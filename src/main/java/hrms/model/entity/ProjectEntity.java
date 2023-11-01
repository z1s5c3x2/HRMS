package hrms.model.entity;


import hrms.model.dto.ProjectDto;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor@NoArgsConstructor
@Getter@Setter@Builder@ToString
@DynamicInsert
@Table(name = "PJT_MNG")
public class ProjectEntity extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pjt_no; //  프로젝트번호
    @ToString.Exclude
    @JoinColumn(name="emp_no")
    @ManyToOne
    private EmployeeEntity emp_no;    // 프로젝트 관리자 사원번호(fk)
    @Column
    private String pjt_name;    // 프로젝트명
    @Column
    private String pjt_st;      // 프로젝트 시작날짜
    @Column
    private String pjt_END;     // 프로젝트 기한
    @Column
    private int pjt_sta;        // 프로젝트 상태
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name="aprv_no")
    private ApprovalEntity aprv_no;            // 결재번호(fk)

    @Builder.Default
    @OneToMany(mappedBy="pjt_no")
    private List<TeamMemberEntity> teamMemberEntities = new ArrayList<>();

    // 1. 전체 출력할때 메소드
    public ProjectDto allToDto(){
        return ProjectDto.builder()
                .pjt_no(this.pjtNo)
                .emp_no(this.empNo.getEmp_no())
                .pjt_name(this.pjtName)
                .pjt_st(this.pjtSt)
                .pjt_END(this.pjtEND)
                .pjt_sta(this.pjtSta)
                .aprv_no((this.aprvNo.getAprv_no()))
                .build();
    }

}
