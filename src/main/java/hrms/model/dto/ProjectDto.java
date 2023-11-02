package hrms.model.dto;

import hrms.model.entity.ProjectEntity;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor@NoArgsConstructor
@Getter@Setter@ToString
@Builder
public class ProjectDto {

    private int pjtNo; //  프로젝트번호
    private String empNo;    // 프로젝트 관리자 사원번호(fk)
    private String pjtName;    // 프로젝트명
    private String pjtSt;      // 프로젝트 시작날짜
    private String pjtEND;     // 프로젝트 기한
    private int pjtSta;        // 프로젝트 상태
    private int aprvNo;            // 결재번호(fk)

    private LocalDateTime cdate;
    private LocalDateTime udate;

    // entity 저장할때 메소드
    public ProjectEntity saveToEntity(){
        return ProjectEntity.builder()
                .pjtName(this.pjtName)
                .pjtSt(this.pjtSt)
                .pjtEND(this.pjtEND)
                .build();
    }

}
