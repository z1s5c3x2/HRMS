package hrms.model.dto;

import hrms.model.entity.DepartmentEntity;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentDto {
    private int dptmNo;                // 부서번호
    private String dptmName;          // 부서이름
    public DepartmentEntity saveToAll()
    {
        return DepartmentEntity.builder()
                .dptmName(this.dptmName)
                .dptmNo(this.dptmNo).build();
    }
}
