package hrms.model.dto;

import lombok.*;

import java.time.LocalDate;

@Getter @Setter @Builder @AllArgsConstructor @NoArgsConstructor
@ToString
public class AttendanceSearchDto {
    private LocalDate periodStart;
    private LocalDate periodEnd;
}
