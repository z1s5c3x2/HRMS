package hrms.controller.test;

import hrms.model.dto.ApprovalRequestDto;
import hrms.model.dto.EmployeeDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
public class ApprovalTest {

    @PostMapping("/post")
    public boolean postMember(@RequestBody ApprovalRequestDto<EmployeeDto> approvalRequestDto) {
        System.out.println(approvalRequestDto);
        EmployeeDto employeeDto = approvalRequestDto.getData();

        System.out.println( employeeDto );

        return false;
    }

}
