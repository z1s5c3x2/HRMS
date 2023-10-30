package hrms.model.entity;



import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Entity
@Table(name = "LRQ")
public class LeaveRequestEntity extends BaseTime { // 휴직/연차/병가 테이블
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private int lrq_no;                           // 연차번호

    @Column()
    private int lrq_type;                      // 2. 타입 ( 1:휴직 2:연차 3:병가 )

    @Column()
    private LocalDateTime lrq_st;            // 3. 시작 날짜

    @Column()
    private LocalDateTime lrq_end;              // 4. 지급유형 ( 1:기본급/2:정기상여/3:특별상여/4:성과금/5:명절휴가비/6:퇴직금)

    @Column()
    private int lrq_srtype;                   // 5. ( 0: 무급 / 1: 유급 )

    @ToString.Exclude
    @JoinColumn(name="emp_no")
    @ManyToOne
    private EmployeeEntity emp_no;                    // 5. 사원번호 ( FK )

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name="aprv_no")
    private ApprovalEntity aprv_no;                            // 결제 테이블 식별 번호( FK)

}
