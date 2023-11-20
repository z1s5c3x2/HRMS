package hrms.controller.approval;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import hrms.model.dto.*;
import hrms.model.entity.ApprovalEntity;
import hrms.model.repository.ApprovalEntityRepository;
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
/*
    // 결재-'수정' 기능 결재 상신 메소드
        // 사원 기본정보(전화번호/비밀번호/계좌번호) / 직급 / 부서변경
        // Approval Type이 다음중 하나인 경우
        // case 2: case 4: case 5: case 7: case 9: case 11: case 13: case 16:
    @PostMapping("/putAproval")
    public boolean putAproval(@RequestBody ApprovalRequestDto<TeamMemberDto> approvalRequestDto) throws JsonProcessingException {
        System.out.println("approvalRequestDto = " + approvalRequestDto);
        // DTO객체 => json 문자열
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String json = objectMapper.writeValueAsString(approvalRequestDto.getData());
        approvalRequestDto.setAprvJson(json);


        // 결재 테이블 등록 메서드
        // => 실행 후 실행결과 반환
        return approvalService.updateApproval(
                approvalRequestDto.getAprvType(),   // 결재타입 [메모장 참고]
                approvalRequestDto.getAprvCont(),   // 결재내용
                approvalRequestDto.getApprovers(),  // 검토자
                approvalRequestDto.getAprvJson()    // 수정할 JSON 문자열
        );

    }
*/
    // 결재-'수정' 기능 결재 상신 메소드
    // 사원 기본정보(전화번호/비밀번호/계좌번호) / 직급 / 부서변경
    // Approval Type이 다음중 하나인 경우
    // case 2: case 4: case 5: case 7: case 9: case 11: case 13: case 16:
    @PostMapping("/putAproval")
    public boolean putAproval(@RequestBody ApprovalRequestDto<TeamMemberDto> approvalRequestDto) throws JsonProcessingException {

        // DTO객체 => json 문자열
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());  // datetime 모듈 추가
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

        return result;
    }

    // 결재-'삭제' 기능 결재 상신 메소드
    @PostMapping("/deleteAproval")
    public boolean deleteAproval(@RequestBody ApprovalRequestDto<Integer> approvalRequestDto) throws JsonProcessingException {

        // DTO객체 => json 문자열
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());  // datetime 모듈 추가
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
    public boolean approbate( @RequestBody ApprovalDto approvalDto ) throws JsonProcessingException {
        return approvalService.approbate( approvalDto.getAprvNo(), approvalDto.getApState() );
    }

    // 전사원 상신목록 조회
    @GetMapping("/getAllEmployeesApproval")
    public PageDto<ApprovalDto> getAllEmployeesApproval(
            @RequestParam int page, @RequestParam String key, @RequestParam String keyword,
            @RequestParam int apState, @RequestParam String strDate, @RequestParam String endDate ) throws JsonProcessingException {

        return approvalService.getAllEmployeesApproval( page, key, keyword, apState, strDate, endDate );
    }

    // 개별 상신목록 조회
    @GetMapping("/getReconsiderHistory")
    public PageDto<ApprovalDto> getReconsiderHistory(
            @RequestParam int page, @RequestParam String key, @RequestParam String keyword,
            @RequestParam int apState, @RequestParam String strDate, @RequestParam String endDate) throws JsonProcessingException {

        return approvalService.getReconsiderHistory( page, key, keyword, apState, strDate, endDate );
    }
    
    // 개별 결재목록 조회
        // 해당 검토자의 검토대상 결재건 / 완료 혹은 반려 결재건
    @GetMapping("/getApprovalHistory")
    public PageDto<ApprovalDto> getApprovalHistory(
            @RequestParam int page, @RequestParam String key, @RequestParam String keyword,
            @RequestParam int apState, @RequestParam String strDate, @RequestParam String endDate ) throws JsonProcessingException {

        return approvalService.getApprovalHistory( page, key, keyword, apState, strDate, endDate );
    }


    // 결재 상세내역 조회
    @GetMapping("/getDetailedApproval")
    public ApprovalEntity getDetailedApproval( int aprvNo ){
        return approvalService.getDetailedApproval( aprvNo );
    }

    @Autowired
    private ApprovalEntityRepository approvalRepository;

    @DeleteMapping("/deleteAapproval")
    public boolean deleteApproval( int aprvNo ){

        try {
            approvalRepository.deleteById( aprvNo );

            return true;
        } catch ( Exception e ){
            return false;
        }

    }




















}
