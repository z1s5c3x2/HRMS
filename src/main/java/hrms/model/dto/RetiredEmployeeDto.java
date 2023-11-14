package hrms.model.dto;

import hrms.model.entity.RetiredEmployeeEntity;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RetiredEmployeeDto {
    private int rtempNo;
    private String rtempCont;
    private LocalDate rtempDate;
    private String empNo;

    public RetiredEmployeeEntity saveToEntity()
    {
        return RetiredEmployeeEntity.builder()
                .rtempDate(this.rtempDate)
                .rtempCont(this.rtempCont).build();
    }
}
