package hrms.service.teamproject;

import hrms.model.dto.ApprovalRequestDto;
import hrms.model.dto.ProjectDto;
import hrms.model.dto.TeamMemberDto;
import hrms.model.entity.ApprovalEntity;
import hrms.model.entity.EmployeeEntity;
import hrms.model.entity.ProjectEntity;
import hrms.model.entity.TeamMemberEntity;
import hrms.model.repository.EmployeeEntityRepository;
import hrms.model.repository.ProjectEntityRepository;
import hrms.model.repository.TeamMemberEntityRepository;
import hrms.service.approval.ApprovalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TeamMemberService {

    @Autowired
    private TeamMemberEntityRepository teamMemberEntityRepository;

    @Autowired
    private ProjectEntityRepository projectEntityRepository;

    @Autowired
    private EmployeeEntityRepository employeeEntityRepository;

    @Autowired
    ApprovalService approvalService;

    // 1. 프로젝트팀 팀원 등록
    @Transactional
    public boolean postTeamMember(ApprovalRequestDto<TeamMemberDto> approvalRequestDto) {


        // 입력한 프로젝트팀 pk번호로 엔티티 호출
        Optional<ProjectEntity> projectEntityOptional =
                projectEntityRepository.findById(approvalRequestDto.getData().getPjtNo());

        // 프로젝트팀 유효성검사
        if(!projectEntityOptional.isPresent()){return false;}

        // 입력한 팀원 pk번호로 엔티티 호출
        Optional<EmployeeEntity> employeeEntityOptional =
                employeeEntityRepository.findByEmpNo(approvalRequestDto.getData().getEmpNo());

        // 사원번호 유효성검사
        if(!employeeEntityOptional.isPresent()){return false;}

        // 결제 테이블 생성
        ApprovalEntity approvalEntity = approvalService.postApproval(
                approvalRequestDto.getAprvType(),
                approvalRequestDto.getAprvCont(),
                approvalRequestDto.getApprovers()
        );

        // 프로젝트팀 팀원 생성
        TeamMemberEntity teamMemberEntity =
                teamMemberEntityRepository.save(approvalRequestDto.getData().saveToEntity());

        // 팀원 엔티티에 팀프로젝트 번호 추가
        teamMemberEntity.setPjtNo(projectEntityOptional.get());

        // 팀원 엔티티에 사원 번호 추가
        teamMemberEntity.setEmpNo(employeeEntityOptional.get());

        // 팀원 엔티티에 결제 번호 추가
        teamMemberEntity.setAprvNo(approvalEntity);

        // 프로젝트팀 엔티티에 팀원 엔티티 추가
        projectEntityOptional.get().getTeamMemberEntities().add(teamMemberEntity);

        // 사원 엔티티에 팀원 엔티티 추가
        employeeEntityOptional.get().getTeamMemberEntities().add(teamMemberEntity);

        // 결재 엔티티에 팀원 엔티티 추가
        approvalEntity.getTeamMemberEntities().add(teamMemberEntity);


        return teamMemberEntity.getTmNo() >= 1;
    }

    // 2. 프로젝트팀 팀원 전체 조회
    public List<TeamMemberDto> getAllTeamMembers(int pjtNo) {

        List<TeamMemberEntity> teamMemberEntities = teamMemberEntityRepository.findAll();
        List<TeamMemberDto> teamMemberDtos = new ArrayList<>();
        
        // 넘겨받은 프로젝트팀번호를 가지고있는 팀원들 엔티티만 dto에 저장
        for(TeamMemberEntity teamMemberEntity : teamMemberEntities){
            if(teamMemberEntity.getPjtNo().getPjtNo() == pjtNo){
                teamMemberDtos.add(teamMemberEntity.oneToDto());
            }
        }

        return teamMemberDtos;
    }



    // 3. 프로젝트팀 팀원 개별 조회
    @Transactional
    public TeamMemberDto getTeamMember(int tmNo){

        // 팀원 리스트에서 팀원번호로 검색후 리턴
        List<TeamMemberEntity> teamMemberEntities = teamMemberEntityRepository.findAll();
        for(TeamMemberEntity teamMemberEntity : teamMemberEntities){
            if(teamMemberEntity.getTmNo() == tmNo){
                return teamMemberEntity.oneToDto();
            }
        }

        return null;
    }


}
