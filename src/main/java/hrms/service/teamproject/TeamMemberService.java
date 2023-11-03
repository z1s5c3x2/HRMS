package hrms.service.teamproject;

import hrms.model.dto.ProjectDto;
import hrms.model.dto.TeamMemberDto;
import hrms.model.entity.EmployeeEntity;
import hrms.model.entity.ProjectEntity;
import hrms.model.entity.TeamMemberEntity;
import hrms.model.repository.EmployeeEntityRepository;
import hrms.model.repository.ProjectEntityRepository;
import hrms.model.repository.TeamMemberEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.transaction.Transactional;
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

    // 1. 팀프로젝트 팀원 등록
    @Transactional
    public boolean postTeamMember(TeamMemberDto teamMemberDto) {


        // 입력한 팀프로젝트 pk번호로 엔티티 호출
        Optional<ProjectEntity> projectEntityOptional =
                projectEntityRepository.findById(teamMemberDto.getPjtNo());

        // 팀프로젝트 유효성검사
        if(!projectEntityOptional.isPresent()){return false;}

        // 입력한 팀원 pk번호로 엔티티 호출
        Optional<EmployeeEntity> employeeEntityOptional =
                employeeEntityRepository.findByEmpNo(teamMemberDto.getEmpNo());

        // 사원번호 유효성검사
        if(!employeeEntityOptional.isPresent()){return false;}

        // 프로젝트 팀원 생성
        TeamMemberEntity teamMemberEntity =
                teamMemberEntityRepository.save(teamMemberDto.saveToEntity());

        // 팀원 엔티티에 팀프로젝트 번호 추가
        teamMemberEntity.setPjtNo(projectEntityOptional.get());

        // 팀원 엔티티에 사원 번호 추가
        teamMemberEntity.setEmpNo(employeeEntityOptional.get());


        return teamMemberEntity.getTmNo() >= 1;
    }

    // 2. 팀프로젝트 팀원 전체 조회



    // 3. 팀프로젝트 팀원 개별 조회
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

    /*// 3. 팀프로젝트 팀원 수정
    @Transactional
    public boolean updateTeamProject(ProjectDto projectDto *//*, ApprovalDto approvalDto*//*){

        return false;
    }*/

}
