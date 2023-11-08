package hrms.controller.teamproject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hrms.model.dto.ApprovalRequestDto;
import hrms.model.dto.ProjectDto;
import hrms.model.dto.TeamMemberDto;
import hrms.service.approval.ApprovalService;
import hrms.service.teamproject.TeamMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/teammember")
public class TeamMemberController {

    @Autowired
    private TeamMemberService teamMemberService;

    @Autowired
    private ApprovalService approvalService;

    // 팀프로젝트 팀원 등록
    @PostMapping("/post")
    public boolean postTeamMember(@RequestBody ApprovalRequestDto<TeamMemberDto> approvalRequestDto) {

        return teamMemberService.postTeamMember(approvalRequestDto);
    }

    // 팀프로젝트 팀원 개별조회
    @GetMapping("/getOne")
    public TeamMemberDto getTeamMember(int tmNo){
        return teamMemberService.getTeamMember(tmNo);
    }

    // 팀프로젝트 팀원 수정
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

}
