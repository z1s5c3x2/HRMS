package hrms.model.dto;


import hrms.model.entity.ApprovalEntity;
import lombok.*;

import java.util.ArrayList;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ApprovalRequestDto<T>{

    private int aprvType;                   // 결재 타입
    private String aprvCont;                // 결재내용
    private String empNo;                   // 상신자
    private String aprvJson;                // json형식의 문자열
    private ArrayList<String> approvers;    // 검토자
    private T data;

    public ApprovalEntity toEntity() {

        return ApprovalEntity.builder()
                .aprvType(this.aprvType)
                .aprvCont(this.aprvCont)
                .build();
    }



}