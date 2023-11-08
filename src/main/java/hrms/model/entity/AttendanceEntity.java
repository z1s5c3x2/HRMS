package hrms.model.entity;

import hrms.model.dto.AttendanceDto;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


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

    //1. //출근 날짜정보가 오늘날짜와 일치하면 (오늘의 출근 정보가 존재하면) true 아니면 false반환

    public boolean toDateOrTime(LocalDateTime dateTime){

        return  dateTime.toLocalDate().toString().equals(LocalDateTime.now().toLocalDate().toString()) ? true: false;

    }

}
