package hrms.model.dto;


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
    private String empNo;
    private int aprvNo;
    // +
    private LocalDateTime cdate;
    private LocalDateTime udate;
    // + 필터용
    private int empRk; // 직급
    private int dptmNo; // 부서번호
    private String empName; // 이름


    //DTO -> entity
    // 1. entity 저장할때
    public SalaryEntity saveToEntity() {
        return SalaryEntity.builder()
                .slryDate(this.slryDate)
                .slryPay(this.slryPay)
                .slryType(this.slryType)
                .build();

    }
}
