package hrms.model.dto;

import hrms.model.entity.ApprovalEntity;
import hrms.model.entity.EmployeeEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
    private LocalDateTime lrq_st;
    private LocalDateTime lrq_end;
    private int lrq_srtype;
    private int emp_no;
    private int aprv_no;
}
