package hrms.service.teamproject;

import hrms.model.dto.ProjectDto;
import hrms.model.entity.EmployeeEntity;
import hrms.model.entity.ProjectEntity;
import hrms.model.repository.EmployeeRepository;
import hrms.model.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class TeamProjectService {

    @Autowired
    private ProjectRepository projectRepository;
    /*
    @Autowired
    private EmployeeService employeeService;
    */

    @Autowired
    private EmployeeRepository employeeRepository;

    // 팀 프로젝트 생성
    @Transactional
    public boolean postTeamProject(ProjectDto projectDto/*, ApprovalDto approvalDto*/){

        projectDto.setAprvNo(1);

        ProjectEntity projectEntity =
                projectRepository.save(projectDto.saveToEntity());

        return projectEntity.getPjtNo() >= 1;
    }

    // 2. 전체 팀프로젝트 출력
    @Transactional
    public List<ProjectDto> getAllTeamProject(){
        List<ProjectEntity> projectEntities = projectRepository.findAll();
        List<ProjectDto> projectDtos = new ArrayList<>();

        for(ProjectEntity projectEntity : projectEntities){
            //projectDtos.add(projectEntities.);
        }

        return null;
    }

    // 3. 팀프로젝트 수정

    // 4. 팀프로젝트 삭제


}
