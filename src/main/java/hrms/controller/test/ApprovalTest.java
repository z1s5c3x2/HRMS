package hrms.controller.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hrms.model.dto.ApprovalRequestDto;
import hrms.model.dto.EmployeeDto;
import hrms.model.entity.ApprovalEntity;
import hrms.service.approval.ApprovalService;
import hrms.service.employee.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")
public class ApprovalTest {

    @Autowired
    ApprovalService approvalService;
    @Autowired
    EmployeeService employeeService;

    @PostMapping("/postAproval")
    public boolean postMember(@RequestBody ApprovalRequestDto<EmployeeDto> approvalRequestDto) {

        // 결재 테이블 등록 메서드
        // => 실행 후 aprvPk 반환
        ApprovalEntity approvalEntity = approvalService.postApproval(
                approvalRequestDto.getAprvType(),   // 결재타입 [메모장 참고]
                approvalRequestDto.getAprvCont(),   // 결재내용
                approvalRequestDto.getApprovers()   // 검토자
        );

        /* 이후 등록 메서드 실행 */
        EmployeeDto data = approvalRequestDto.getData();

        return false;
    }

    @PostMapping("/putAproval")
    public boolean putAproval(@RequestBody ApprovalRequestDto<EmployeeDto> approvalRequestDto) throws JsonProcessingException {

        // DTO객체 => json 문자열
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(approvalRequestDto.getData());
        approvalRequestDto.setAprvJson(json);

        // 결재 테이블 등록 메서드
        // => 실행 후 aprvPk 반환
        boolean result = approvalService.updateApproval(
                approvalRequestDto.getAprvType(),   // 결재타입 [메모장 참고]
                approvalRequestDto.getAprvCont(),   // 결재내용
                approvalRequestDto.getApprovers(),  // 검토자
                approvalRequestDto.getAprvJson()    // 수정할 JSON 문자열
        );

        return false;
    }


    @PutMapping("/putMember")
    public boolean updateMember( @RequestParam int aprvNo ) throws JsonProcessingException {
        System.out.printf("aprvNo : "+aprvNo);
        employeeService.setEmployeeInfo( aprvNo );

        return false;

    }


}
