package hrms.model.dto;


import lombok.*;

import java.util.ArrayList;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ApprovalRequestDto<T> {

    private int aprvType;                   // 결재 타입
    private String aprvCont;                // 결재내용
    private int empNo;                      // 상신자
    private ArrayList<Integer> approvers;   // 검토자
    private T data;


}