package hrms.service.teamproject;

import hrms.model.dto.ProjectDto;
import hrms.model.entity.ApprovalEntity;
import hrms.model.entity.ApprovalLogEntity;
import hrms.model.entity.EmployeeEntity;
import hrms.model.entity.ProjectEntity;
import hrms.model.repository.EmployeeEntityRepository;
import hrms.model.repository.ProjectEntityRepository;
import hrms.service.employee.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TeamProjectService {

    @Autowired
    private ProjectEntityRepository projectRepository;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeEntityRepository employeeRepository;

    // 팀 프로젝트 생성
    @Transactional
    public boolean postTeamProject(ProjectDto projectDto/*, ApprovalDto approvalDto*/){

        // 입력한 팀프로젝트 관리자 pk번호 호출
        Optional<EmployeeEntity> employeeEntityOptional =
                employeeRepository.findByEmpNo(projectDto.getEmpNo());

        // 사원번호 유효성검사
        if(!employeeEntityOptional.isPresent()){return false;}

        // 팀 프로젝트 생성
        ProjectEntity projectEntity =
                projectRepository.save(projectDto.saveToEntity());
        // 팀 프로젝트에 관리자 사원번호 추가
        projectEntity.setEmpNo(employeeEntityOptional.get());

        // 사원 엔티티에 팀 프로젝트 엔티티 추가
        employeeEntityOptional.get().getProjectEntities().add(projectEntity);

        return projectEntity.getPjtNo() >= 1;
    }

    // 2. 전체 팀프로젝트 출력(승인된 팀프로젝트만)
    @Transactional
    public List<ProjectDto> getAllTeamProject(){
        List<ProjectEntity> projectEntities = projectRepository.findAll();
        List<ProjectDto> projectDtos = new ArrayList<>();

        for(ProjectEntity projectEntity : projectEntities){
            List<ApprovalLogEntity> approvalLogEntities = projectEntity.getAprvNo().getApprovalLogEntities();

            for(ApprovalLogEntity approvalLogEntity : approvalLogEntities){

            }
                projectDtos.add(projectEntity.allToDto());
                System.out.println(projectEntity.allToDto());
        }

        return projectDtos;
    }
    
    // 3. 개별 프로젝트 조회(팀원정보도 출력)
    @Transactional
    public ProjectDto getOneTeamProject(int pjtNo){

        // 팀프로젝트 리스트에서 개별 팀프로젝트 검색후 리턴
        List<ProjectEntity> projectEntities = projectRepository.findAll();
        for(ProjectEntity projectEntity : projectEntities){
            if(projectEntity.getPjtNo() == pjtNo){
                return projectEntity.oneToDto();
            }
        }

        return null;
    }

    // 4. 팀프로젝트 수정
    @Transactional
    public boolean updateTeamProject(ProjectDto projectDto /*, ApprovalDto approvalDto*/){

        return false;
    }

    // 5. 팀프로젝트 삭제


}
