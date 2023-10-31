package hrms.model.dto;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RetiredEmployeeDto {
    private int rtemp_no;
    private String rtemp_cont;
    private String rtemp_date;
    private int emp_no;

}
