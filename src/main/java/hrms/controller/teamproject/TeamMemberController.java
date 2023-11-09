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

import java.util.List;

@RestController
@RequestMapping("/teammember")
public class TeamMemberController {

    @Autowired
    private TeamMemberService teamMemberService;

    @Autowired
    private ApprovalService approvalService;

    // 프로젝트팀 팀원 등록
    @PostMapping("/post")
    public boolean postTeamMember(@RequestBody ApprovalRequestDto<TeamMemberDto> approvalRequestDto) {

        return teamMemberService.postTeamMember(approvalRequestDto);
    }

    // 프로젝트팀 팀원 개별조회
    @GetMapping("/getOne")
    public TeamMemberDto getTeamMember(int tmNo){
        return teamMemberService.getTeamMember(tmNo);
    }

    // 팀프로젝트 팀원 전체조회
    @GetMapping("/getAll")
    public List<TeamMemberDto> getAllTeamMembers(@RequestParam int pjtNo){

        return teamMemberService.getAllTeamMembers(pjtNo);
    }

    // 팀프로젝트 팀원 수정
    // @PostMapping("/putAproval") ---> 매핑잡아주기


    // 프로젝트팀 팀원 삭제
    // @PostMapping("/deleteAproval") ---> 매핑잡아주기


}
