package hrms.service.teamproject;

import hrms.model.dto.ApprovalRequestDto;
import hrms.model.dto.EmployeeDto;
import hrms.model.dto.ProjectDto;
import hrms.model.entity.ApprovalEntity;
import hrms.model.entity.ApprovalLogEntity;
import hrms.model.entity.EmployeeEntity;
import hrms.model.entity.ProjectEntity;
import hrms.model.repository.EmployeeEntityRepository;
import hrms.model.repository.ProjectEntityRepository;
import hrms.service.approval.ApprovalService;
import hrms.service.employee.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
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

    @Autowired
    ApprovalService approvalService;

    // 팀 프로젝트 생성
    @Transactional
    public boolean postTeamProject(ApprovalRequestDto<ProjectDto> approvalRequestDto){

        // 입력한 팀프로젝트 관리자 pk번호 호출
        Optional<EmployeeEntity> employeeEntityOptional =
                employeeRepository.findByEmpNo(approvalRequestDto.getData().getEmpNo());

        // 사원번호 유효성검사
        if(!employeeEntityOptional.isPresent()){return false;}

        // 결제 테이블 생성
        /*approvalService.postApproval(
                approvalRequestDto.getAprvType(),
                approvalRequestDto.getAprvCont(),
                approvalRequestDto.getEmpNo(),
                approvalRequestDto.getApprovers()
        );*/

        // 팀 프로젝트 생성
        ProjectEntity projectEntity =
                projectRepository.save( approvalRequestDto.getData().saveToEntity() );
        // 팀 프로젝트에 관리자 사원번호 추가
        projectEntity.setEmpNo(employeeEntityOptional.get());

        // 사원 엔티티에 팀 프로젝트 엔티티 추가
        employeeEntityOptional.get().getProjectEntities().add(projectEntity);

        return projectEntity.getPjtNo() >= 1;
    }

    // 전체 팀프로젝트 출력
    @Transactional
    public List<ProjectDto> getAllTeamProject(){

        List<ProjectEntity> projectEntities = projectRepository.findAll();
        List<ProjectDto> projectDtos = new ArrayList<>();

        for(ProjectEntity projectEntity : projectEntities){
            projectDtos.add(projectEntity.allToDto());
        }

        return projectDtos;
    }

    // 3. 팀프로젝트 출력(approval이 1 = 승인, 2 = 반려, 3 = 검토중)
    @Transactional
    public List<ProjectDto> getPermitAllTeamProject(int approval) {
        List<ProjectEntity> projectEntities = projectRepository.findAll();
        List<ProjectDto> projectDtos = new ArrayList<>();

        for (ProjectEntity projectEntity : projectEntities) {
            List<ApprovalLogEntity> approvalLogEntities = projectEntity.getAprvNo().getApprovalLogEntities();
            boolean allStaThree = true;     // 검토중 판단
            boolean hasRejection = false;   // 반려상태 판단

            for (ApprovalLogEntity approvalLogEntity : approvalLogEntities) {
                int aplogSta = approvalLogEntity.getAplogSta();

                if (aplogSta == 2) {
                    hasRejection = true;
                } else if (aplogSta != 1) {
                    allStaThree = false;
                }
            }

            if (approval == 1 && allStaThree) {
                // 모두 1(승인) 상태일 때 승인
                projectDtos.add(projectEntity.allToDto());
            } else if (approval == 2 && hasRejection) {
                // 2(반려) 상태가 하나라도 있을 때 반려
                projectDtos.add(projectEntity.allToDto());
            } else if (approval == 3 && !allStaThree) {
                // 나머지 경우의 수, 3(검토중) 상태가 있을 때 검토중
                projectDtos.add(projectEntity.allToDto());
            }
        }

        return projectDtos;
    }

    
    // 5. 개별 프로젝트 조회(팀원정보도 출력)
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

    // 6. 팀프로젝트 수정
    @Transactional
    public boolean updateTeamProject(ProjectDto projectDto /*, ApprovalDto approvalDto*/){

        return false;
    }

    // 7. 팀프로젝트 삭제
    @Transactional
    public boolean deleteTeamProject(int pjtNo){

        return false;
    }


}
