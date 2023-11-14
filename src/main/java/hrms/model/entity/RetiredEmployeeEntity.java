package hrms.model.entity;


import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity @Table(name="RTEMP")
public class RetiredEmployeeEntity extends BaseTime {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int rtempNo;
    @Column( )
    private String rtempCont;
    @Column()
    private LocalDate rtempDate;

    @ToString.Exclude
    @JoinColumn(name="empNo")
    @ManyToOne
    private EmployeeEntity empNo;
    @ToString.Exclude
    @JoinColumn(name="aprvNo")
    @ManyToOne
    private ApprovalEntity aprvNo;


}
