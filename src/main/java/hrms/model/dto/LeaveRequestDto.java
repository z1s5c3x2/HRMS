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
    private int lrqNo;
    private int lrqType;
    private LocalDate lrqSt;
    private LocalDate lrqEnd;
    private int lrqSrtype;
    private String empNo;
    private int aprvNo;
    // +
    private LocalDateTime cdate;
    private LocalDateTime udate;
    // +
    private String empName;

    //DTO -> entity
    // 1. entity 저장할때
    public LeaveRequestEntity saveToEntity( ){
        return LeaveRequestEntity.builder()
                .lrqType(this.lrqType)
                .lrqSt(this.lrqSt)
                .lrqEnd(this.lrqEnd)
                .lrqSrtype(this.lrqSrtype)
                .build();

    }
}
