package hrms.model.entity;

import hrms.model.dto.LeaveRequestDto;
import hrms.model.dto.SalaryDto;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Entity
@Table(name = "SLRY")
public class SalaryEntity extends BaseTime {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private int slry_no;            // 1. 급여 식별번호 ( PK )

    @Column()
    private LocalDate slry_date;      // 2. 급여 지급 날짜

    @Column()
    private int slry_pay;                // 3. 지급 금액

    @Min(1)
    @Max(6)
    @Column()
    private int slry_type;               // 4. 지급유형 ( 1:기본급/2:정기상여/3:특별상여/4:성과금/5:명절휴가비/6:퇴직금)

    @ToString.Exclude
    @JoinColumn(name="emp_no")
    @ManyToOne
    private EmployeeEntity emp_no;        // 5. 사원번호 ( FK )

    @ToString.Exclude
    @JoinColumn(name="aprv_no")
    @ManyToOne
    private ApprovalEntity aprv_no;     // 6.결제번호

    // 1. 전체 출력
    public SalaryDto allToDto(){
        return  SalaryDto.builder()
                .slry_no(this.slry_no)
                .slry_date(this.slry_date)
                .slry_pay(this.slry_pay)
                .slry_type(this.slry_type)
                .emp_no(this.emp_no.getEmp_no())
                .aprv_no(this.aprv_no.getAprv_no())
                .cdate(this.getCdate()).udate(this.getUdate())
                .build();
    }
}
