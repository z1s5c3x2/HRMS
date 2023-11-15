package hrms.model.dto;

import lombok.*;

import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString @Builder
public class DepartmentHistoryDto {
    private int htrdpNo;
    private LocalDate hdptmStart;
    private LocalDate hdptmEnd;
    private int aprvNo;
    private String empNo;
    private int dptmNo;
}
