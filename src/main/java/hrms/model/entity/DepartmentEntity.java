package hrms.model.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="DPTM")
public class DepartmentEntity extends BaseTime{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int dptm_no;                // 부서번호
    @Column( )
    private String dptm_name;          // 부서이름

    @Builder.Default
    @OneToMany(mappedBy = "dptm_no")
    private List<DepartmentHistoryEntity> departmentHistory = new ArrayList<>();
    @Builder.Default
    @OneToMany(mappedBy = "dptmNo")
    private List<EmployeeEntity> employeeEntities = new ArrayList<>();
}
