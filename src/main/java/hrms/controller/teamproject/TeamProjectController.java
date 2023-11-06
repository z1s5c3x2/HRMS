package hrms.controller.teamproject;

import hrms.model.dto.ApprovalRequestDto;
import hrms.model.dto.EmployeeDto;
import hrms.model.dto.ProjectDto;
import hrms.service.teamproject.TeamProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teamproject")
public class TeamProjectController {

    @Autowired
    private TeamProjectService teamProjectService;

    // 1. 팀프로젝트 생성
    @PostMapping("/post")
    public boolean postTeamProject(@RequestBody ApprovalRequestDto<ProjectDto> approvalRequestDto){
        boolean result = teamProjectService.postTeamProject(approvalRequestDto);

        return result;
    }

    // 전체 팀프로젝트 출력
    @GetMapping("/getAll")
    public List<ProjectDto> getAllTeamProject(){return teamProjectService.getAllTeamProject();}

    // 2. 승인,반려, 검토중 상태에 따른 팀프로젝트 출력
    @GetMapping("/getPermitAll")
    public List<ProjectDto> getPermitAllTeamProject(@RequestParam int approval){
        return teamProjectService.getPermitAllTeamProject(approval);
    }


    // 3. 개별 팀프로젝트 출력
    @GetMapping("/getOne")
    public ProjectDto getOneTeamProject(@RequestParam int pjtNo){
        return teamProjectService.getOneTeamProject(pjtNo);
    }

    // 4. 팀프로젝트 수정

    // 5. 팀프로젝트 삭제

}
