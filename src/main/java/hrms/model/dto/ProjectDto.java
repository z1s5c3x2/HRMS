package hrms.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import hrms.model.entity.ProjectEntity;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor@NoArgsConstructor
@Getter@Setter@ToString
@Builder
public class ProjectDto {

    private int pjtNo; //  프로젝트번호
    private String empNo;    // 프로젝트 관리자 사원번호(fk)
    private String empName;    // 프로젝트 관리자 사원이름
    private String pjtName;    // 프로젝트명
    private LocalDate pjtSt;      // 프로젝트 시작날짜
    private LocalDate pjtEND;     // 프로젝트 기한
    private int pjtSta;        // 프로젝트 상태
    private int aprvNo;            // 결재번호(fk)

    private List<TeamMemberDto> teamMembers;        // 팀원 리스트

    private LocalDateTime cdate;
    private LocalDateTime udate;

    // entity 저장할때 메소드
    public ProjectEntity saveToEntity(){
        return ProjectEntity.builder()
                .pjtName(this.pjtName)
                .pjtSt(this.pjtSt.plusDays(1))
                .pjtEND(this.pjtEND.plusDays(1))
                .build();
    }

}
