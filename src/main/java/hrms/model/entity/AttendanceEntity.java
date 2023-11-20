package hrms.model.entity;

import hrms.model.dto.AttendanceDto;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;


@DynamicInsert
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Entity
@Table(name = "attd")
public class AttendanceEntity extends BaseTime{
    //필드설계
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto-increment,
    private int attdNo;       // 근태기록번호(식별)

    @Column()
    private String attdWrst;         // 출근시간
    @Column()
    private String attdWrend;           // 퇴근 시간

    @ToString.Exclude
    @JoinColumn(name="empNo")
    @ManyToOne
    private EmployeeEntity empNo;        // 5. 사원번호 ( FK )


    public AttendanceDto allToDto() {
        return AttendanceDto.builder()
                .attdNo(this.attdNo)
                .attdWrst(this.attdWrst)
                .attdWrend(this.attdWrend)
                .empNo(this.empNo.getEmpNo())
                .build();
    }

    public AttendanceDto allEmpListDto(){
        return AttendanceDto.builder()
                .attdNo(this.attdNo)
                .attdWrend(this.attdWrend)
                .attdWrst(this.attdWrst)
                .empRk(this.empNo.getEmpRk())
                .empName(this.empNo.getEmpName())
                .empSta(this.empNo.isEmpSta())
                .dptmName(this.empNo.getDptmNo().getDptmName())
                .build();
    }

}
