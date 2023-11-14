package hrms.service.approval;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import hrms.model.dto.ApprovalDto;
import hrms.model.dto.EmployeeDto;
import hrms.model.dto.ProjectDto;
import hrms.model.dto.TeamMemberDto;
import hrms.model.entity.*;
import hrms.model.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ApprovalService {

    @Autowired
    private ApprovalEntityRepository approvalRepository;
    @Autowired
    private ApprovalLogEntityRepository approvalLogRepository;
    @Autowired
    private EmployeeEntityRepository employeeRepository;
    @Autowired
    private DepartmentEntityRepository departmentEntityRepository;
    @Autowired
    private ProjectEntityRepository projectRepository;
    @Autowired
    private TeamMemberEntityRepository teamMemberRepository;

    @Transactional // 로그가 남는 변경 기능 업데이트 정보 까지 받아서 올린 후 pk 반환
    public ApprovalEntity updateLogApproval(int aprvType, String aprvCont, ArrayList<String> approvers, String aprvJson)
    {
        // 상신자
        // 추후 세션 호출 또는 userDetails 호출에 대한 구문기입 예정
        Optional<EmployeeEntity> optionalEmployeeEntity = employeeRepository.findByEmpNo( "2311004" );

        if( optionalEmployeeEntity.isPresent() ) {

            ApprovalEntity approvalEntity = ApprovalEntity
                    .builder()
                    .aprvType(aprvType)
                    .aprvJson(aprvJson)
                    .aprvCont(aprvCont)
                    .empNo(optionalEmployeeEntity.get())
                    .build();
            // DB 저장
            ApprovalEntity result = approvalRepository.save( approvalEntity );
            /* 단방향 */
            // 검토자에 대한 사원테이블 JPA 단방향 관계 정립
            result.setEmpNo( optionalEmployeeEntity.get() );
            /* 양방향 */
            // 사원테이블 JPA 단방향 관계 정립
            optionalEmployeeEntity.get().getApprovalEntities().add( result );

            // 검토자 DB 저장을 위한 메서드 실행
            postApprovalLog( approvers, result.getAprvNo() );

            if( result.getAprvNo() >= 1 )  return result;
        }

        return null;
    }

    // 최초등록 : 결재 테이블 등록 [등록 기능에 관한 테이블]
    @Transactional
    public ApprovalEntity postApproval(int aprvType, String aprvCont, ArrayList<String> approvers) {

        /*
        타입 정리가 확실히 될 시 유효성 검사 추가 예정

        if( aprvType == 0 ){
            return null;
        }
        */

        // 상신자
        // 추후 세션 호출 또는 userDetails 호출에 대한 구문기입 예정
        Optional<EmployeeEntity> optionalEmployeeEntity = employeeRepository.findByEmpNo("2311004");

        if (optionalEmployeeEntity.isPresent()) {

            ApprovalEntity approvalEntity = ApprovalEntity
                    .builder()
                    .aprvType(aprvType)
                    .aprvCont(aprvCont)
                    .empNo(optionalEmployeeEntity.get())
                    .build();

            // DB 저장
            ApprovalEntity result = approvalRepository.save(approvalEntity);
            /* 단방향 */
            // 검토자에 대한 사원테이블 JPA 단방향 관계 정립
            result.setEmpNo(optionalEmployeeEntity.get());
            /* 양방향 */
            // 사원테이블 JPA 단방향 관계 정립
            optionalEmployeeEntity.get().getApprovalEntities().add(result);

            // 검토자 DB 저장을 위한 메서드 실행
            postApprovalLog(approvers, result.getAprvNo());

            if (result.getAprvNo() >= 1) return result;

        }
        return null;
    }


    // 수정 : 결재 테이블 JSON문자열 저장 [수정 기능에 관한 테이블]
    @Transactional
    public boolean updateApproval(int aprvType, String aprvCont, ArrayList<String> approvers, String aprvJson) {

        /*
        타입 정리가 확실히 될 시 유효성 검사 추가 예정

        if( aprvType == 0 ){
            return null;
        }
        */
        System.out.println(222);
        // 상신자
        // 추후 세션 호출 또는 userDetails 호출에 대한 구문기입 예정
        Optional<EmployeeEntity> optionalEmployeeEntity = employeeRepository.findByEmpNo("2311004");
        System.out.println(333);
        if (optionalEmployeeEntity.isPresent()) {
            System.out.println(444);
            ApprovalEntity approvalEntity = ApprovalEntity
                    .builder()
                    .aprvType(aprvType)
                    .aprvCont(aprvCont)
                    .aprvJson(aprvJson)
                    .empNo(optionalEmployeeEntity.get())
                    .build();
            System.out.println(5555);
            // DB 저장
            ApprovalEntity result = approvalRepository.save(approvalEntity);
            /* 단방향 */
            // 검토자에 대한 사원테이블 JPA 단방향 관계 정립
            result.setEmpNo(optionalEmployeeEntity.get());
            System.out.println(666);
            /* 양방향 */
            // 사원테이블 JPA 단방향 관계 정립
            optionalEmployeeEntity.get().getApprovalEntities().add(result);
            System.out.println(777);
            // 검토자 DB 저장을 위한 메서드 실행
            postApprovalLog(approvers, result.getAprvNo());

            if (result.getAprvNo() >= 1) return true;

        }

        return false;
    }


    // 결재 검토자 테이블에 검토자 등록
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    public boolean postApprovalLog(ArrayList<String> approvers, int aprvNo) {

        // 해당 결재건을 조회
        Optional<ApprovalEntity> optionalApproval = approvalRepository.findById(aprvNo);
        Optional<EmployeeEntity> optionalEmployee;

        // 검토자 목록을 순회하며 각 검토자에 대한 ApprovalLogEntity를 생성하고 설정
        for (String approver : approvers) {

            // 검토자 1명에 대한 optional EMP 객체 생성
            optionalEmployee = employeeRepository.findByEmpNo(approver);

            if (optionalEmployee.isPresent() && optionalApproval.isPresent()) {

                /* 단방향 */
                // 결재 검토자에 대한 객체 생성
                ApprovalLogEntity logEntity = ApprovalLogEntity
                        .builder()
                        .aplogSta(3)
                        .empNo(optionalEmployee.get())
                        .aprvNo(optionalApproval.get())
                        .build();
                // DB 저장
                ApprovalLogEntity result = approvalLogRepository.save(logEntity);
                // 결재테이블 JPA 단방향 관계 정립
                result.setAprvNo(optionalApproval.get());
                // 사원테이블 JPA 단방향 관계 정립
                result.setEmpNo(optionalEmployee.get());

                /* 양방향 */
                // 결재테이블 JPA 양방향 관계 정립
                optionalApproval.get().getApprovalLogEntities().add(result);
                // 사원테이블 JPA 양방향 관계 정립
                optionalEmployee.get().getApprovalLogs().add(result);

                if (result.getAplogNo() < 1) return false;

            }
        }

        return false;

    }

    // 검토자 1명 승인
    @Transactional
    public boolean approbate(int aprvNo, int aplogSta) throws JsonProcessingException {

        // 추후 세션 호출 또는 userDetails 호출에 대한 구문기입 예정
        // 검토자
        Optional<EmployeeEntity> optionalEmployee = employeeRepository.findByEmpNo("2311004");
        Optional<ApprovalEntity> optionalApproval = approvalRepository.findById(aprvNo);


        if (!optionalApproval.isPresent() || !optionalEmployee.isPresent()) return false;

        Optional<ApprovalLogEntity> optionalApprovallogEntity = approvalLogRepository.findByAprvNoAndEmpNo(
                optionalApproval.get(), optionalEmployee.get());

        switch (aplogSta) {
            // 결재
            case 1:
                optionalApprovallogEntity.get().setAplogSta(1);
                break;
            // 반려
            case 2:
                optionalApprovallogEntity.get().setAplogSta(2);
                break;
            default:
                return false;
        }

        // 해당 검토자가 최종 검토자일 경우
        if (approvalLogRepository.findMinAplogNoByAprvNo(optionalApproval.get()) == optionalApprovallogEntity.get().getAplogNo()) {
            // 결재의 종류가 수정(put)일 경우
            // 기존 DB에 저장된 문자열JSON 이용하여 DB UPDATE 실행
            switch (optionalApproval.get().getAprvType()) {

                // 사원정보 수정
                case 2:
                case 4:
                case 5:
                    return updateMemberInfoAproval(optionalApproval.get().getAprvNo());

                // (휴직/연차/병가) 기간 변경 수정
                case 7:
                case 9:
                case 11:
                    return updateLeaveRequestInfoAproval(optionalApproval.get().getAprvNo());

                // 프로젝트 수정
                case 13:
                    return updateProjectInfoAproval(optionalApproval.get().getAprvNo());

                // 프로젝트 / 프로젝트 팀원 삭제
                case 14:
                case 17:
                    return deleteProjectApproval(optionalApproval.get().getAprvNo());

                // 프로젝트 팀 사원 수정
                case 16:
                    return updateTeamMemberApproval(optionalApproval.get().getAprvNo());

            }
        }

        return true;
    }

    // 결재건에 대한 결재 진행상태 확인 ( 1:결재완료  2:반려  3:검토중 )
    public int checkApprovalState(ApprovalEntity approvalEntity) {

        // 1개의 결재건에 대한 검토자리스트 호출
        for (int i = 0; i < approvalEntity.getApprovalLogEntities().size(); i++) {

            // 1명의 검토자 비교
            switch (approvalEntity.getApprovalLogEntities().get(i).getAplogSta()) {

                case 2:
                    return 2;   // 결재상태 : 반려
                case 1:
                    return 1;   // 결재상태 : 완료
                case 3:
                    return 3;   // 결재상태 : 검토중

            }
        }
        return -1;
    }


    // 결재 완료된 사원 '수정' 기능 메서드
    @Transactional
    public boolean updateMemberInfoAproval(int aprvNo) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, true);

        Optional<ApprovalEntity> optionalApprovalEntity = approvalRepository.findById(aprvNo);
        if (!optionalApprovalEntity.isPresent()) return false;

        // JSON문자열 => DTO객체로 변환
        EmployeeDto employeeDto
                = objectMapper.readValue(optionalApprovalEntity.get().getAprvJson(), EmployeeDto.class);
        // DTO => Entity 객체로 변환
        EmployeeEntity employeeEntity = employeeDto.saveToEntity();

        // 수정 전 해당 결재수정안에 대한 data 호출
        // 변경 대상사원 객체 호출
        Optional<EmployeeEntity> optionalEmployeeEntity = employeeRepository.findByEmpNo(employeeEntity.getEmpNo());

        if (optionalEmployeeEntity.isPresent()) {

            // 결재 타입에 따른 DB변경 실행
            switch (optionalApprovalEntity.get().getAprvType()) {

                // 사원 기본정보 변경(전화번호/비밀번호/계좌번호)
                case 2:
                    optionalEmployeeEntity.get().setEmpPhone(employeeEntity.getEmpPhone());
                    optionalEmployeeEntity.get().setEmpPwd(employeeEntity.getEmpPwd());
                    optionalEmployeeEntity.get().setEmpAcn(employeeEntity.getEmpAcn());
                    break;

                // 사원 부서변경
                case 4:

                    Optional<DepartmentEntity> optionalDepartmentEntity = departmentEntityRepository.findById(employeeDto.getDtpmNo());
                    if (!optionalDepartmentEntity.isPresent()) return false;

                    optionalEmployeeEntity.get().setDptmNo(optionalDepartmentEntity.get());
                    employeeRepository.save(optionalEmployeeEntity.get());

                    break;

                // 사원 직급변경
                case 5:
                    optionalEmployeeEntity.get().setEmpRk(employeeEntity.getEmpRk());
                    break;

            }
            return true;
        }
        return false;

    }

    // 결재 완료된 (휴직/연차/병가) 기간/급여지급여부 '수정' 기능 메서드
    @Transactional
    public boolean updateLeaveRequestInfoAproval(int aprvNo) throws JsonProcessingException {

        return false;
    }

    // 결재 완료된 프로젝트 '수정' 기능 메서드
    @Transactional
    public boolean updateProjectInfoAproval(int aprvNo) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, true);

        Optional<ApprovalEntity> optionalApprovalEntity = approvalRepository.findById(aprvNo);
        if (!optionalApprovalEntity.isPresent()) return false;

        // JSON문자열 => DTO객체로 변환
        ProjectDto projectDto
                = objectMapper.readValue(optionalApprovalEntity.get().getAprvJson(), ProjectDto.class);

        // DTO => Entity 객체로 변환
        ProjectEntity projectEntity = projectDto.saveToEntity();

        // 수정 전 해당 결재수정안에 대한 data 호출
        // 변경대상 프로젝트 호출
        Optional<ProjectEntity> optionalProjectEntity = projectRepository.findById(projectDto.getPjtNo());

        if (optionalProjectEntity.isPresent()) {

            // 수정 PM ENTITY 호출
            Optional<EmployeeEntity> pmEntity = employeeRepository.findByEmpNo(projectDto.getEmpNo());
            // 프로젝트 변경
            optionalProjectEntity.get().setEmpNo(pmEntity.get());                 // 프로젝트PM
            optionalProjectEntity.get().setPjtName(projectEntity.getPjtName());   // 프로젝트명
            optionalProjectEntity.get().setPjtSt(projectEntity.getPjtSt());       // 시작일자
            optionalProjectEntity.get().setPjtEND(projectEntity.getPjtEND());     // 마감일자
            optionalProjectEntity.get().setPjtSta(projectEntity.getPjtSta());     // 프로젝트 진행상태 ( 0:진행x 1:진행중 )

            projectRepository.save(optionalProjectEntity.get());

        }

        return false;
    }

    // 결재 완료된 팀 멤버 '수정' 기능 메서드
    @Transactional
    public boolean updateTeamMemberApproval(int aprvNo) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, true);

        Optional<ApprovalEntity> optionalApprovalEntity = approvalRepository.findById(aprvNo);
        if (!optionalApprovalEntity.isPresent()) return false;

        // JSON문자열 => DTO객체로 변환
        TeamMemberDto teamMemberDto
                = objectMapper.readValue(optionalApprovalEntity.get().getAprvJson(), TeamMemberDto.class);

        // ※ DTO => Entity 객체로 변환할 생성자가 없으므로 생략

        // 수정 전 해당 결재수정안에 대한 data 호출
        // 변경대상 프로젝트 팀원 호출
        Optional<ProjectEntity> optionalProjectEntity = projectRepository.findById(teamMemberDto.getTmNo());

        if (optionalProjectEntity.isPresent()) {

            // 수정 PM ENTITY 호출
            Optional<TeamMemberEntity> optionalTeamMemberEntity = teamMemberRepository.findById(teamMemberDto.getTmNo());
            // 프로젝트 변경
            optionalTeamMemberEntity.get().setTmNo(teamMemberDto.getTmNo());
            optionalTeamMemberEntity.get().setTmSt(teamMemberDto.getTmSt());
            optionalTeamMemberEntity.get().setTmEnd(teamMemberDto.getTmEnd());

            projectRepository.save(optionalProjectEntity.get());

        }

        return false;
    }

    // 결재 완료된 프로젝트 '삭제' 기능 메서드
    @Transactional
    public boolean deleteProjectApproval(int aprvNo) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, true);

        Optional<ApprovalEntity> optionalApprovalEntity = approvalRepository.findById(aprvNo);
        if (!optionalApprovalEntity.isPresent()) return false;

        // JSON문자열 => 객체로 변환
        int typeNo = optionalApprovalEntity.get().getAprvType();
        int idNo = objectMapper.readValue(optionalApprovalEntity.get().getAprvJson(), Integer.class);

        switch (typeNo) {

            // 프로젝트 삭제
            case 14:
                Optional<ProjectEntity> optionalProjectEntity = projectRepository.findById(idNo);
                if (!optionalProjectEntity.isPresent()) return false;
                projectRepository.deleteById(idNo);
                return true;

            // 프로젝트 팀원 삭제
            case 17:
                Optional<TeamMemberEntity> optionalTeamMemberEntity = teamMemberRepository.findById(idNo);
                if (!optionalTeamMemberEntity.isPresent()) return false;
                teamMemberRepository.deleteById(idNo);
                return true;
        }

        return false;
    }


    // 개별 상신목록 조회
    public List<ApprovalDto> getReconsiderHistory() {

        // 상신자
        // 추후 세션 호출 또는 userDetails 호출에 대한 구문기입 예정
        Optional<EmployeeEntity> optionalEmployeeEntity = employeeRepository.findByEmpNo("2311004");

        // 개별 상신(결재)내역 전체 조회
        List<ApprovalEntity> approvalList
                = approvalRepository.findByAllempNo(optionalEmployeeEntity.get().getEmpNo());

        // 유효성 검사
        // 값이 비어있으면 true / null이면 false
        if (approvalList.isEmpty()) return null;

        // 변환할 DTO 리스트
        List<ApprovalDto> approvalDtos = new ArrayList<>();

        approvalList.forEach(e -> {

            // 변환된 DTO 리스트 삽입
            approvalDtos.add(e.toApprovalDto());
            // 마지막 추가된 DTO
            // => 현재 결재 진행상태를 확인하여 저장
            approvalDtos.get(approvalDtos.size() - 1).setApState(checkApprovalState(e));

        });

        return approvalDtos;
    }

    // 개별 결재목록 조회
    public List<ApprovalDto> getApprovalHistory() {

        // 결재자
// 추후 "2311004" => userDetails 기입 예정
        Optional<EmployeeEntity> optionalEmployeeEntity = employeeRepository.findByEmpNo("ccc");

        // 개별 결재내역 전체 조회
        List<ApprovalLogEntity> approvalLogList
                = approvalLogRepository.findByEmpNo(optionalEmployeeEntity.get());

        // 검토자에 해당 되는 결재건 탐색
        return approvalLogList.stream().map(e -> {
            
            // 반환할 DTO
            ApprovalDto approvalDto = e.getAprvNo().toApprovalDto();

            // 해당 검토자가 검토를 완료했을 경우
            // AplogSta(결재상태)   = 1:완료  2:반려
            if(e.getAplogSta() == 1) {
                approvalDto.setApState(1);  // 결재완료 여부 저장 후 반환
                return approvalDto;
            }
            if(e.getAplogSta() == 2){
                approvalDto.setApState(2);  // 결재완료 여부 저장 후 반환
                return approvalDto;
            }
            
            // 해당 결재 건에 대한 다수의 검토자 결재여부 탐색 실시
            // ※ '반려' 혹은 '검토 중'일 시 탐색대상에서 제외
            for (int i = 0; i < e.getAprvNo().getApprovalLogEntities().size(); i++) {
                // 탐색 중 본인에 해당되는 인덱스 식별
                // - 본인이 첫 번째 검토자일 경우
// 추후 "2311004" => userDetails 기입 예정
                if (e.getAprvNo().getApprovalLogEntities().get(i).getEmpNo().getEmpNo().equals("ccc")
                        && i == 0) {
                    approvalDto.setApState(3);  // 결재완료 여부 저장 후 반환
                    return approvalDto;
                }
                // - 이전 검토자가 결재를 '완료' 하였을 경우
// 추후 "2311004" => userDetails 기입 예정
                if (e.getAprvNo().getApprovalLogEntities().get(i).getEmpNo().getEmpNo().equals("ccc")
                        && e.getAprvNo().getApprovalLogEntities().get(i - 1).getAplogSta() == 1) {
                    approvalDto.setApState(3);  // 결재완료 여부 저장 후 반환
                    return approvalDto;
                }
            }

            return null;

        })
        .filter(Objects::nonNull) // null을 제외하고 필터링
        .collect(Collectors.toList());

    }


}
