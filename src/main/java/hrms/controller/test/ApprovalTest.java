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
        /*
        System.out.println(approvalRequestDto);
        approvalService.postApproval(
                approvalRequestDto.getAprvType(),
                approvalRequestDto.getAprvCont(),
                approvalRequestDto.getEmpNo(),
                approvalRequestDto.getApprovers()
        );

        EmployeeDto employeeDto = approvalRequestDto.getData();

        System.out.println( employeeDto );
*/
        return false;
    }

}
