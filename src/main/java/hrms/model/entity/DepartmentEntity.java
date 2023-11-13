package hrms.model.entity;

import hrms.model.dto.DepartmentDto;
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
    private int dptmNo;                // 부서번호
    @Column( )
    private String dptmName;          // 부서이름
    @Builder.Default
    @OneToMany( fetch = FetchType.LAZY  , cascade = CascadeType.ALL ,mappedBy = "dptmNo")
    private List<DepartmentHistoryEntity> departmentHistory = new ArrayList<>();
    @Builder.Default
    @OneToMany( fetch = FetchType.LAZY  , cascade = CascadeType.ALL ,mappedBy = "dptmNo")
    private List<EmployeeEntity> employeeEntities = new ArrayList<>();

    public DepartmentDto allToDto() {
        return DepartmentDto.builder()
                .dptmName(this.dptmName)
                .dptmNo(this.dptmNo).build();
    }
}
