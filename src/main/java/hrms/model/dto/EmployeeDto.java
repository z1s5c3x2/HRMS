package hrms.model.dto;

import hrms.model.entity.EmployeeEntity;
import lombok.*;

@Getter @Setter  @Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDto {
    private int emp_no;                     // 사원번호
    private String emp_name;                // 사원이름
    private String emp_phone;               // 사원전환번호
    private String emp_pwd;                 // 사원비밀번호
    private String emp_sex;                 // 사원성별
    private String emp_acn;                 // 사원계좌번호
    private boolean emp_sta;                 // 근무상태
    private int emp_rk;                     // 사원 직급

    public EmployeeEntity saveToEntity()
    {
        return EmployeeEntity.builder()
                .emp_no(this.emp_no)
                .emp_name(this.emp_name)
                .emp_phone(this.emp_phone)
                .emp_pwd(this.emp_pwd)
                .emp_sex(this.emp_sex)
                .emp_acn(this.emp_acn)
                .emp_sta(this.emp_sta)
                .emp_rk(this.emp_rk).build();
    }
}
