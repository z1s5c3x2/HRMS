package hrms.model.entity;


import hrms.model.dto.ProjectDto;
import hrms.model.dto.TeamMemberDto;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private int pjtNo; //  프로젝트번호
    @ToString.Exclude
    @JoinColumn(name="empNo")
    @ManyToOne
    private EmployeeEntity empNo;    // 프로젝트 관리자 사원번호(fk)
    @Column
    private String pjtName;    // 프로젝트명
    @Column
    private LocalDate pjtSt;      // 프로젝트 시작날짜
    @Column
    private LocalDate pjtEND;     // 프로젝트 기한
    @Column(name = "pjtSta")       // 프로젝트 상태 디폴트값(1 = 진행중)
    @ColumnDefault("1")
    private int pjtSta;        // 프로젝트 상태
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name="aprvNo")
    private ApprovalEntity aprvNo;            // 결재번호(fk)

    @Builder.Default
    @OneToMany( fetch = FetchType.LAZY  , cascade = CascadeType.ALL ,mappedBy="pjtNo")
    private List<TeamMemberEntity> teamMemberEntities = new ArrayList<>();

    // 1. 전체 출력할때 메소드
    public ProjectDto allToDto(){
        return ProjectDto.builder()
                .pjtNo(this.pjtNo)
                .empNo(this.empNo.getEmpNo())
                .empName(this.empNo.getEmpName())
                .pjtName(this.pjtName)
                .pjtSt(this.pjtSt)
                .pjtEND(this.pjtEND)
                .pjtSta(this.pjtSta)
                .aprvNo((this.aprvNo.getAprvNo()))
                .build();
    }

    // 2. 개별 조회시 메소드
    public ProjectDto oneToDto(){
        return ProjectDto.builder()
                .pjtNo(this.pjtNo)
                .empNo(this.empNo.getEmpNo())
                .empName(this.empNo.getEmpName())
                .pjtName(this.pjtName)
                .pjtSt(this.pjtSt)
                .pjtEND(this.pjtEND)
                .pjtSta(this.pjtSta)
                .aprvNo((this.aprvNo.getAprvNo()))
                .teamMembers(getTeamMembers(this.teamMemberEntities))
                .build();
    }

    // 3. 팀원리스트 변환 메소드
    public List<TeamMemberDto> getTeamMembers(List<TeamMemberEntity> teamMemberEntities){
        List<TeamMemberDto> teamMemberDtos = new ArrayList<>();

        for(TeamMemberEntity teamMemberEntity : teamMemberEntities){
            teamMemberDtos.add(teamMemberEntity.oneToDto());
        }
        return teamMemberDtos;
    }

}
