package hrms.model.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@DynamicInsert
@Table( name = "APLOG" )
// 결재내역테이블
public class ApprovalLogEntity extends BaseTime{

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY  )
    private int aplog_no;            // 결재내역번호
    @Column()
    @ColumnDefault( "3" )
    private int aplog_sta;         // (1결재 / 2반려 / 3검토중)
    @ToString.Exclude
    @JoinColumn(name="emp_no")
    @ManyToOne
    private EmployeeEntity emp_no;             // fk 검토자
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name="aprv_no")
    private ApprovalEntity aprv_no;             // fk 결제테이블





}
