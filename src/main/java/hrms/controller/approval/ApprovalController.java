package hrms.controller.approval;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hrms.model.dto.ApprovalDto;
import hrms.model.dto.ApprovalRequestDto;
import hrms.model.dto.EmployeeDto;
import hrms.model.dto.TeamMemberDto;
import hrms.model.entity.ApprovalEntity;
import hrms.service.approval.ApprovalService;
import hrms.service.employee.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/approval")
public class ApprovalController {
    @Autowired
    ApprovalService approvalService;
    @Autowired
    EmployeeService employeeService;

    // 결재-'등록' 기능 결재 상신 메소드
    @PostMapping("/postAproval")
    public boolean postMember(@RequestBody ApprovalRequestDto<EmployeeDto> approvalRequestDto) {

        // 결재 테이블 등록 메서드
        // => 실행 후 aprv엔티티 객체 반환
        ApprovalEntity approvalEntity = approvalService.postApproval(
                approvalRequestDto.getAprvType(),   // 결재타입 [메모장 참고]
                approvalRequestDto.getAprvCont(),   // 결재내용
                approvalRequestDto.getApprovers()   // 검토자
        );

        /* 이후 등록 메서드 실행 */
        EmployeeDto data = approvalRequestDto.getData();

        return false;
    }

    // 결재-'수정' 기능 결재 상신 메소드
        // 사원 기본정보(전화번호/비밀번호/계좌번호) / 직급 / 부서변경
        // Approval Type이 다음중 하나인 경우
        // case 2: case 4: case 5: case 7: case 9: case 11: case 13: case 16:
    @PostMapping("/putAproval")
    public boolean putAproval(@RequestBody ApprovalRequestDto<TeamMemberDto> approvalRequestDto) throws JsonProcessingException {

        // DTO객체 => json 문자열
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(approvalRequestDto.getData());
        approvalRequestDto.setAprvJson(json);

        // 결재 테이블 등록 메서드
        // => 실행 후 실행결과 반환
        boolean result = approvalService.updateApproval(
                approvalRequestDto.getAprvType(),   // 결재타입 [메모장 참고]
                approvalRequestDto.getAprvCont(),   // 결재내용
                approvalRequestDto.getApprovers(),  // 검토자
                approvalRequestDto.getAprvJson()    // 수정할 JSON 문자열
        );

        return false;
    }

    @PostMapping("/deleteAproval")
    public boolean deleteAproval(@RequestBody ApprovalRequestDto<Integer> approvalRequestDto) throws JsonProcessingException {

        // DTO객체 => json 문자열
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(approvalRequestDto.getData());
        approvalRequestDto.setAprvJson(json);

        // 결재 테이블 등록 메서드
        // => 실행 후 실행결과 반환
        return  approvalService.updateApproval(
                approvalRequestDto.getAprvType(),   // 결재타입 [메모장 참고]
                approvalRequestDto.getAprvCont(),   // 결재내용
                approvalRequestDto.getApprovers(),  // 검토자
                approvalRequestDto.getAprvJson()    // 수정할 JSON 문자열
        );

    }

    // 검토자 1명 승인
    @PutMapping("/approbate")
    public boolean approbate( @RequestParam int aprvNo, @RequestParam int aplogSta ) throws JsonProcessingException {
        // aplogSta : 1:결재 / 2: 반려 / 3:검토중

        return approvalService.approbate( aprvNo, aplogSta );
    }

    // 개별 상신목록 조회
    @GetMapping("/getReconsiderHistory")
    public List<ApprovalDto> getApprovalHistory() throws JsonProcessingException {

        return approvalService.getReconsiderHistory();
    }
    
    // 개별 결재목록 조회
























}
