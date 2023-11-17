package hrms.service.teamproject;

import hrms.model.dto.ApprovalRequestDto;
import hrms.model.dto.EmployeeDto;
import hrms.model.dto.PageDto;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

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

    // 프로젝트팀 생성
    @Transactional
    public boolean postTeamProject(ApprovalRequestDto<ProjectDto> approvalRequestDto){

        // 입력한 프로젝트팀 관리자 pk번호 호출
        Optional<EmployeeEntity> employeeEntityOptional =
                employeeRepository.findByEmpNo(approvalRequestDto.getData().getEmpNo());

        // 사원번호 유효성검사
        if(!employeeEntityOptional.isPresent()){return false;}

        // 결재 테이블 생성
        ApprovalEntity approvalEntity = approvalService.postApproval(
                approvalRequestDto.getAprvType(),
                approvalRequestDto.getAprvCont(),
                approvalRequestDto.getApprovers()
        );

        // 프로젝트팀 생성
        ProjectEntity projectEntity =
                projectRepository.save( approvalRequestDto.getData().saveToEntity() );
        // 프로젝트팀에 관리자 사원번호 추가
        projectEntity.setEmpNo(employeeEntityOptional.get());

        // 프로젝트팀에 결재 번호 추가
        projectEntity.setAprvNo(approvalEntity);

        // 사원 엔티티에 프로젝트팀 엔티티 추가
        employeeEntityOptional.get().getProjectEntities().add(projectEntity);

        // 결재 엔티티에 프로젝트팀 엔티티 추가
        approvalEntity.getProjectEntities().add(projectEntity);

        return projectEntity.getPjtNo() >= 1;
    }

    // 전체 프로젝트팀 출력
    @Transactional
    public PageDto getAllTeamProject(int page){

        // 페이징처리 라이브러리
        Pageable pageable = PageRequest.of(page-1, 5);

        Page<ProjectEntity> projectEntities = projectRepository.findAll(pageable);
        List<ProjectDto> projectDtos = new ArrayList<>();

        for(ProjectEntity projectEntity : projectEntities){
            projectDtos.add(projectEntity.allToDto());
        }

        // 총 페이지수
        int totalPages = projectEntities.getTotalPages();
        // 총 게시물수
        long totalCount = projectEntities.getTotalElements();

        // pageDto를 구성해서 전달
        // 제네릭 타입의 List를 구성하기위해 PageDto.<ProjectDto>builder() 형식으로 사용
        PageDto<ProjectDto> pageDto = PageDto.<ProjectDto>builder()
                .someList(projectDtos)
                .totalPages(totalPages)
                .totalCount(totalCount)
                .build();

        return pageDto;
    }

    // 3. 프로젝트팀 출력(approval이 1 = 승인, 2 = 반려, 3 = 검토중)
    @Transactional
    public PageDto getPermitAllTeamProject(int approval, int page) {
        // 페이징처리 라이브러리
        // 한페이지에 보여질 팀프로젝트 수
        Pageable pageable = PageRequest.of(page-1, 5);
        List<ProjectDto> projectDtos = new ArrayList<>();

        // 총 페이지수
        int totalPages = 0;
        // 총 게시물수
        long totalCount = 0;

        if(approval == 1){
            Page<ProjectEntity> projectEntities = projectRepository.findByAllAplogStaIs1(pageable);

            for(ProjectEntity projectEntity : projectEntities){
                projectDtos.add(projectEntity.allToDto());
            }

            totalPages = projectEntities.getTotalPages();
            totalCount = projectEntities.getTotalElements();

        }

        if(approval == 2){
            Page<ProjectEntity> projectEntities = projectRepository.findProjectsByApprovalLogAplogSta2(pageable);

            for(ProjectEntity projectEntity : projectEntities){
                projectEntity.setPjtSta(1);
                projectDtos.add(projectEntity.allToDto());
            }

            totalPages = projectEntities.getTotalPages();
            totalCount = projectEntities.getTotalElements();
        }

        if(approval == 3){
            Page<ProjectEntity> projectEntities = projectRepository.findProjectsByApprovalLog(pageable);

            for(ProjectEntity projectEntity : projectEntities){
                projectDtos.add(projectEntity.allToDto());
            }

            totalPages = projectEntities.getTotalPages();
            totalCount = projectEntities.getTotalElements();
        }




        /*for (ProjectEntity projectEntity : projectEntities) {
            List<ApprovalLogEntity> approvalLogEntities = projectEntity.getAprvNo().getApprovalLogEntities();
            boolean allStaThree = true;     // 검토중 판단
            boolean hasRejection = false;   // 반려상태 판단
            System.out.println(approvalLogEntities);

            for (ApprovalLogEntity approvalLogEntity : approvalLogEntities) {
                int aplogSta = approvalLogEntity.getAplogSta();
                System.out.println(aplogSta);

                if (aplogSta == 2) {
                    hasRejection = true;
                } else if (aplogSta == 3) {
                    allStaThree = false;
                }
            }

            // 각 경우의 수마다 totalCount 1씩증가
            if (approval == 1 && allStaThree) {
                // 모두 1(승인) 상태일 때 승인
                projectDtos.add(projectEntity.allToDto());
            } else if (approval == 2 && hasRejection) {
                // 2(반려) 상태가 하나라도 있을 때 반려
                projectDtos.add(projectEntity.allToDto());
            } else if(approval == 3 && !allStaThree){
                // 나머지 경우의 수, 3(검토중) 상태가 있을 때 검토중
                projectDtos.add(projectEntity.allToDto());
            }
        }*/

        // PageDto구성
        PageDto<ProjectDto> pageDto = PageDto.<ProjectDto>builder()
                .someList(projectDtos)
                .totalPages(totalPages)
                .totalCount(totalCount)
                .build();

        return pageDto;
    }

    public List<ProjectDto> getSelectTeamProject(int approval){

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

            // 각 경우의 수마다 totalCount 1씩증가
            if (approval == 1 && allStaThree) {
                // 모두 1(승인) 상태일 때 승인
                projectDtos.add(projectEntity.allToDto());
            } else if (approval == 2 && hasRejection) {
                // 2(반려) 상태가 하나라도 있을 때 반려
                projectDtos.add(projectEntity.allToDto());
            } else if(approval == 3){
                // 나머지 경우의 수, 3(검토중) 상태가 있을 때 검토중
                projectDtos.add(projectEntity.allToDto());
            }
        }

        System.out.println("projectDtos = " + projectDtos);

        return projectDtos;
    }

    
    // 5. 개별 프로젝트팀 조회(팀원정보도 출력)
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

    // 6. 프로젝트팀 수정(approvalService 에서 진행)

    // 7. 프로젝트팀 삭제(approvalService 에서 진행)



}
