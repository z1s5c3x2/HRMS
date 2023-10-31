package hrms.model.dto;

import hrms.model.entity.ProjectEntity;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor@NoArgsConstructor
@Getter@Setter@ToString
@Builder
public class ProjectDto {

    private int pjt_no; //  프로젝트번호
    private int emp_no;    // 프로젝트 관리자 사원번호(fk)
    private String pjt_name;    // 프로젝트명
    private String pjt_st;      // 프로젝트 시작날짜
    private String pjt_END;     // 프로젝트 기한
    private int pjt_sta;        // 프로젝트 상태
    private int aprv_no;            // 결재번호(fk)

    private LocalDateTime cdate;
    private LocalDateTime udate;

    // entity 저장할때 메소드
    public ProjectEntity saveToEntity(){
        return ProjectEntity.builder()
                .pjt_name(this.pjt_name)
                .pjt_st(this.pjt_st)
                .pjt_END(this.pjt_END)
                .build();
    }

}
