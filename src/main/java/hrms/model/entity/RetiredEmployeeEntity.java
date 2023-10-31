package hrms.model.entity;


import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity @Table(name="RTEMP")
public class RetiredEmployeeEntity extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int rtemp_no;

    @Column()
    private String rtemp_cont;
    @Column()
    private String rtemp_date;

    @ToString.Exclude
    @JoinColumn(name="emp_no")
    @ManyToOne
    private EmployeeEntity emp_no;

    @ToString.Exclude
    @JoinColumn(name="aprv_no")
    @ManyToOne
    private ApprovalEntity aprv_no;


}
