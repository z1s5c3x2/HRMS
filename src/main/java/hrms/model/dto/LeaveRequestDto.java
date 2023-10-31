package hrms.model.dto;

import hrms.model.entity.ApprovalEntity;
import hrms.model.entity.EmployeeEntity;
import hrms.model.entity.LeaveRequestEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class LeaveRequestDto {
    private int lrq_no;
    private int lrq_type;
    private LocalDate lrq_st;
    private LocalDate lrq_end;
    private int lrq_srtype;
    private int emp_no;
    private int aprv_no;
    // +
    private LocalDateTime cdate;
    private LocalDateTime udate;

    //DTO -> entity
    // 1. entity 저장할때
    public LeaveRequestEntity saveToEntity( ){
        return LeaveRequestEntity.builder()
                .lrq_type(this.lrq_type)
                .lrq_st(this.lrq_st)
                .lrq_end(this.lrq_end)
                .lrq_srtype(this.lrq_srtype)
                .build();

    }
}
