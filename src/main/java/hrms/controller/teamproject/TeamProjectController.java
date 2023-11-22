package hrms.controller.teamproject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import hrms.model.dto.*;
import hrms.service.approval.ApprovalService;
import hrms.service.teamproject.TeamProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teamproject")
public class TeamProjectController {

    @Autowired
    private TeamProjectService teamProjectService;

    @Autowired
    private ApprovalService approvalService;

    // 1. 팀프로젝트 생성
    @PostMapping("/post")
    public boolean postTeamProject(@RequestBody ApprovalRequestDto<ProjectDto> approvalRequestDto){
        boolean result = teamProjectService.postTeamProject(approvalRequestDto);

        return result;
    }

    // 전체 팀프로젝트 출력
    @GetMapping("/getAll")
    public PageDto getAllTeamProject(@RequestParam int page){
        return teamProjectService.getAllTeamProject(page);
    }

    // 2. 승인,반려, 검토중 상태에 따른 팀프로젝트 출력
    @GetMapping("/getPermitAll")
    public PageDto getPermitAllTeamProject(
            @RequestParam int approval,
            @RequestParam int page){
        return teamProjectService.getPermitAllTeamProject(approval, page);
    }

    // 2-2. 승인,반려, 검토중 상태에 따른 팀프로젝트 출력(프로젝트 선택용)
    @GetMapping("/getSelectAll")
    public List<ProjectDto> getSelectTeamProject(@RequestParam int approval){
        return teamProjectService.getSelectTeamProject(approval);
    }

    // 3. 개별 팀프로젝트 출력
    @GetMapping("/getOne")
    public ProjectDto getOneTeamProject(@RequestParam int pjtNo){
        return teamProjectService.getOneTeamProject(pjtNo);
    }

    // 4. 팀프로젝트 수정
    @PostMapping("/putAproval")
    public boolean putAproval(@RequestBody ApprovalRequestDto<ProjectDto> approvalRequestDto) throws JsonProcessingException {

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


    // 5. 팀프로젝트 삭제
    // @PostMapping("/deleteAproval") ---> 매핑잡아주기

}
