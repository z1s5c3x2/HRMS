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
    private int aplogNo;            // 결재내역번호
    @Column()
    @ColumnDefault( "3" )
    private int aplogSta;         // (1결재 / 2반려 / 3검토중)
    @ToString.Exclude
    @JoinColumn(name="empNo")
    @ManyToOne
    private EmployeeEntity empNo;             // fk 검토자

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name="aprvNo")
    private ApprovalEntity aprvNo;             // fk 결제테이블





}













