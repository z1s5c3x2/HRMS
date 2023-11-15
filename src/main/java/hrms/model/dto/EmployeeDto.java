package hrms.model.dto;

import hrms.model.entity.EmployeeEntity;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter @Setter  @Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDto implements UserDetails {

    List<GrantedAuthority> authList;

    @Override   // 계정 권환 목록(여러개의 권한을 가질 수 있음)
    public Collection<? extends GrantedAuthority> getAuthorities() {return authList;}

    @Override   // 계정 비밀번호
    public String getPassword() {return empPwd;}

    @Override   // 계정 아이디
    public String getUsername() {return empNo;}

    @Override   // 계정 완료 여부
    public boolean isAccountNonExpired() {return true;}

    @Override   // 계정 잠금 여부
    public boolean isAccountNonLocked() {return true;}

    @Override   // 계정 증명 여부
    public boolean isCredentialsNonExpired() {return true;}

    @Override   // 계정 활성화 여부
    public boolean isEnabled() {return true;}


    // -------------------------------------------------------




    private String empNo;                     // 사원번호
    private String empName;                // 사원이름
    private String empPhone;               // 사원전환번호
    private String empPwd;                 // 사원비밀번호
    private String empSex;                 // 사원성별
    private String empAcn;                 // 사원계좌번호
    private boolean empSta;                 // 근무상태
    private int empRk;                     // 사원 직급
    private int restCnt;                    //연차 조회
    private int dptmNo;                    // 부서 번호
    private String dptmName;                //부서 이름
    private String empNewPwd;             //  정보 수정시 새로운 패스워드
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
