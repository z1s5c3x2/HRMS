package hrms.model.dto;

import hrms.model.entity.AttendanceEntity;
import hrms.model.entity.EmployeeEntity;
import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class AttendanceDto {
    //필드설계
    private int attd_no;                // 근태기록번호(식별)
    private String attd_wrst;           // 출근시간
    private String attd_wrend;          // 퇴근 시간
    private EmployeeEntity emp_no;      // 사원번호

    public AttendanceEntity toEntity() {
        return AttendanceEntity.builder()
                .attd_no(this.attd_no)
                .attd_wrst(this.attd_wrst)
                .attd_wrend(this.attd_wrend)
                .emp_no(this.emp_no)
                .build();
    }
}
