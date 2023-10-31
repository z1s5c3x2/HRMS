package hrms.model.dto;


import hrms.model.entity.LeaveRequestEntity;
import hrms.model.entity.SalaryEntity;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class SalaryDto {
    private int slry_no;
    private LocalDate slry_date;
    private int slry_pay;
    private int slry_type;
    private int emp_no;
    private int aprv_no;
    // +
    private LocalDateTime cdate;
    private LocalDateTime udate;

    //DTO -> entity
    // 1. entity 저장할때
    public SalaryEntity saveToEntity( ){
        return SalaryEntity.builder()
                .slry_date(this.slry_date)
                .slry_pay(this.slry_pay)
                .slry_type(this.slry_type)
                .build();

    }
}
