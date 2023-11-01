package hrms.model.dto;

import hrms.model.entity.EmployeeEntity;
import hrms.model.entity.RetiredEmployeeEntity;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RetiredEmployeeDto {
    private int rtempNo;
    private String rtempCont;
    private String rtempDate;
    private int empNo;

    public RetiredEmployeeEntity saveToEntity()
    {
        return RetiredEmployeeEntity.builder()
                .rtempDate(this.rtempDate)
                .rtempCont(this.rtempCont).build();
    }
}
