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
    private int slryNo;
    private LocalDate slryDate;
    private int slryPay;
    private int slryType;
    private int empNo;
    private int aprvNo;
    // +
    private LocalDateTime cdate;
    private LocalDateTime udate;

    //DTO -> entity
    // 1. entity 저장할때
    public SalaryEntity saveToEntity( ){
        return SalaryEntity.builder()
                .slryDate(this.slryDate)
                .slryPay(this.slryPay)
                .slryType(this.slryType)
                .build();

    }
}
