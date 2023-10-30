package hrms.model.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
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
    private String slry_date;      // 2. 급여 지급 날짜

    @Column()
    private int slry_pay;                // 3. 지급 금액

    @Column()
    private int slry_type;               // 4. 지급유형 ( 1:기본급/2:정기상여/3:특별상여/4:성과금/5:명절휴가비/6:퇴직금)

    @ToString.Exclude
    @JoinColumn(name="emp_no")
    @ManyToOne
    private EmployeeEntity emp_no;        // 5. 사원번호 ( FK )
    @ToString.Exclude
    @JoinColumn(name="aprv_no")
    @ManyToOne
    private ApprovalEntity aprv_no;
}
