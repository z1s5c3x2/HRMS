package hrms.model.entity;


import hrms.model.dto.LeaveRequestDto;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDate;


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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int lrqNo;                           // 연차번호

    @Max(3)
    @Min(1)
    @Column()
    private int lrqType;                      // 2. 타입 ( 1:휴직 2:연차 3:병가 )

    @Column()
    private LocalDate lrqSt;            // 3. 시작 날짜

    @Column()
    private LocalDate lrqEnd;              // 4. 종료 날짜

    @Max(1)
    @Min(0)
    @Column()
    private int lrqSrtype;                   // 5. ( 0: 무급 / 1: 유급 )

    @ToString.Exclude
    @JoinColumn(name = "empNo")
    @ManyToOne
    private EmployeeEntity empNo;                    // 5. 사원번호 ( FK )

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "aprvNo")
    private ApprovalEntity aprvNo;

    // 1. 전체 출력
    public LeaveRequestDto allToDto() {
        return LeaveRequestDto.builder()
                .lrqNo(this.lrqNo)
                .lrqType(this.lrqType)
                .lrqSt(this.lrqSt)
                .lrqEnd(this.lrqEnd)
                .lrqSrtype(this.lrqSrtype)
                .empNo(this.empNo.getEmpNo())
                .aprvNo(this.aprvNo.getAprvNo())
                .cdate(this.getCdate()).udate(this.getUdate())
                .empRk(this.empNo.getEmpRk())
                .dptmNo(this.empNo.getDptmNo().getDptmNo())
                .empName(this.empNo.getEmpName())
                .build();
    }

    // 1. 개별출력
    public LeaveRequestDto OneToDto() {
        return LeaveRequestDto.builder()
                .lrqNo(this.lrqNo)
                .lrqType(this.lrqType)
                .lrqSt(this.lrqSt)
                .lrqEnd(this.lrqEnd)
                .lrqSrtype(this.lrqSrtype)
                .empNo(this.empNo.getEmpNo())
                .aprvNo(this.aprvNo.getAprvNo())
                .cdate(this.getCdate()).udate(this.getUdate())
                .empRk(this.empNo.getEmpRk())
                .dptmNo(this.empNo.getDptmNo().getDptmNo())
                .empName(this.empNo.getEmpName())
                .build();
    }

}
