package hrms.controller.test;

import hrms.model.dto.ApprovalRequestDto;
import hrms.model.dto.EmployeeDto;
import hrms.service.approval.ApprovalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
public class ApprovalTest {

    @Autowired
    ApprovalService approvalService;

    @PostMapping("/post")
    public boolean postMember(@RequestBody ApprovalRequestDto<EmployeeDto> approvalRequestDto) {

        // 결재 테이블 등록 메서드
        // => 실행 후 aprvPk 반환
        int aprvPk = approvalService.postApproval(
                approvalRequestDto.getAprvType(),   // 결재타입 [메모장 참고]
                approvalRequestDto.getAprvCont(),   // 결재내용
                approvalRequestDto.getApprovers()   // 검토자
        );

        /* 이후 등록 메서드 실행 */

        return false;
    }

}
