package hrms.model.entity;



import hrms.model.dto.LeaveRequestDto;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDate;
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

    @Max(3)
    @Min(1)
    @Column()
    private int lrq_type;                      // 2. 타입 ( 1:휴직 2:연차 3:병가 )

    @Column()
    private LocalDate lrq_st;            // 3. 시작 날짜

    @Column()
    private LocalDate lrq_end;              // 4. 종료 날짜

    @Max(1)
    @Min(0)
    @Column()
    private int lrq_srtype;                   // 5. ( 0: 무급 / 1: 유급 )

    @ToString.Exclude
    @JoinColumn(name="emp_no")
    @ManyToOne
    private EmployeeEntity emp_no;                    // 5. 사원번호 ( FK )

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name="aprv_no")
    private ApprovalEntity aprv_no;

    // 1. 전체 출력
    public LeaveRequestDto allToDto(){
        return  LeaveRequestDto.builder()
                .lrq_no(this.lrq_no)
                .lrq_type(this.lrq_type)
                .lrq_st(this.lrq_st)
                .lrq_end(this.lrq_end)
                .lrq_srtype(this.lrq_srtype)
                .emp_no(this.emp_no.getEmp_no())
                .aprv_no(this.aprv_no.getAprv_no())
                .cdate(this.getCdate()).udate(this.getUdate())
                .build();
    }

}
