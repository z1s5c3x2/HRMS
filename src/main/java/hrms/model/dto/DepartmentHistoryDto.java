package hrms.model.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString @Builder
public class DepartmentHistoryDto {
    private int htrdpNo;
    private String dtpmStart;
    private String dtpmEnd;
    private int targetDptm;
    private int aprvNo;
    private String empNo;
    private int dtpmNo;
}
