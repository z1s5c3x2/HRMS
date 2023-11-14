package hrms.model.dto;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeSearchDto {
    private String empNo;                     // 사원번호
    private String empName;                // 사원이름
    private String empSex;                 // 사원성별
    private String empAcn;                 // 사원계좌번호
    private boolean empSta;                 // 근무상태
    private int empRk;                     // 사원 직급
    private String dptmName;                    //부서
    private int aprvCount;                  //해당 사원이 올린 완료 안된 결제 수
    private int apLogCount;                 //해당 사원이 처리해야 하는 결제 수
}
