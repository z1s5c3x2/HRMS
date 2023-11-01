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
    private int lrqNo;
    private int lrqType;
    private LocalDateTime lrqSt;
    private LocalDateTime lrqEnd;
    private int lrqSrtype;
    private int empNo;
    private int aprvNo;
}
