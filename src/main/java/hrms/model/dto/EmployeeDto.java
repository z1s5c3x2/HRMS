package hrms.model.dto;

import hrms.model.entity.EmployeeEntity;
import lombok.*;

@Getter @Setter  @Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDto {
    private int empNo;                     // 사원번호
    private String empName;                // 사원이름
    private String empPhone;               // 사원전환번호
    private String empPwd;                 // 사원비밀번호
    private String empSex;                 // 사원성별
    private String empAcn;                 // 사원계좌번호
    private boolean empSta;                 // 근무상태
    private int empRk;                     // 사원 직급

    private int dtpmNo;
    public EmployeeEntity saveToEntity()
    {
        return EmployeeEntity.builder()
                .empNo(this.empNo)
                .empName(this.empName)
                .empPhone(this.empPhone)
                .empPwd(this.empPwd)
                .empSex(this.empSex)
                .empAcn(this.empAcn)
                .empSta(this.empSta)
                .empRk(this.empRk).build();
    }
}
