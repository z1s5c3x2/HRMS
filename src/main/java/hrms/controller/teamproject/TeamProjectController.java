package hrms.controller.teamproject;

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
    public boolean postTeamProject(@RequestBody ProjectDto projectDto /*, ApprovalDto approvalDto*/){
        boolean result = teamProjectService.postTeamProject(projectDto/*, approvalDto*/);

        return result;
    }

    // 2. 전체 팀프로젝트 출력
    @GetMapping("/getAll")
    public List<ProjectDto> getAllTeamProject(){
        return teamProjectService.getAllTeamProject();
    }

    // 3. 개별 팀프로젝트 출력
    @GetMapping("/getOne")
    public ProjectDto getOneTeamProject(@RequestParam int pjtNo){
        return teamProjectService.getOneTeamProject(pjtNo);
    }

    // 4. 팀프로젝트 수정

    // 5. 팀프로젝트 삭제

}
