package hrms.service.approval;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.org.apache.xalan.internal.xsltc.trax.XSLTCSource;
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
import java.util.Optional;

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
    private  ProjectEntityRepository projectRepository;
    @Autowired
    private  TeamMemberEntityRepository teamMemberRepository;


    // 최초등록 : 결재 테이블 등록 [등록 기능에 관한 테이블]
    @Transactional
    public ApprovalEntity postApproval(int aprvType, String aprvCont, ArrayList<String> approvers ){

        /*
        타입 정리가 확실히 될 시 유효성 검사 추가 예정

        if( aprvType == 0 ){
            return null;
        }
        */

        // 추후 세션 호출 또는 userDetails 호출에 대한 구문기입 예정
        Optional<EmployeeEntity> optionalEmployeeEntity = employeeRepository.findByEmpNo( "2311004" );

        if( optionalEmployeeEntity.isPresent() ){

            ApprovalEntity approvalEntity = ApprovalEntity
                    .builder()
                    .aprvType(aprvType)
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


    // 수정 : 결재 테이블 JSON문자열 저장 [수정 기능에 관한 테이블]
    @Transactional
    public boolean updateApproval(int aprvType, String aprvCont, ArrayList<String> approvers, String aprvJson ) {

        /*
        타입 정리가 확실히 될 시 유효성 검사 추가 예정

        if( aprvType == 0 ){
            return null;
        }
        */

        // 추후 세션 호출 또는 userDetails 호출에 대한 구문기입 예정
        Optional<EmployeeEntity> optionalEmployeeEntity = employeeRepository.findByEmpNo( "2311004" );

        if( optionalEmployeeEntity.isPresent() ){

            ApprovalEntity approvalEntity = ApprovalEntity
                    .builder()
                    .aprvType(aprvType)
                    .aprvCont(aprvCont)
                    .aprvJson(aprvJson)
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

            if( result.getAprvNo() >= 1 )  return true;

        }

        return false;
    }


    // 결재 검토자 테이블에 검토자 등록
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    public boolean postApprovalLog( ArrayList<String> approvers, int aprvNo ) {

        // 해당 결재건을 조회
        Optional<ApprovalEntity> optionalApproval = approvalRepository.findById( aprvNo );
        Optional<EmployeeEntity> optionalEmployee;

        // 검토자 목록을 순회하며 각 검토자에 대한 ApprovalLogEntity를 생성하고 설정
        for (String approver : approvers) {

            // 검토자 1명에 대한 optional EMP 객체 생성
            optionalEmployee = employeeRepository.findByEmpNo( approver );

            if (optionalEmployee.isPresent() && optionalApproval.isPresent()) {

                /* 단방향 */
                // 결재 검토자에 대한 객체 생성
                ApprovalLogEntity logEntity = ApprovalLogEntity
                        .builder()
                        .aplogSta(3)
                        .empNo( optionalEmployee.get() )
                        .aprvNo( optionalApproval.get() )
                        .build();
                // DB 저장
                ApprovalLogEntity result = approvalLogRepository.save( logEntity );
                // 결재테이블 JPA 단방향 관계 정립
                result.setAprvNo( optionalApproval.get() );
                // 사원테이블 JPA 단방향 관계 정립
                result.setEmpNo( optionalEmployee.get() );

                /* 양방향 */
                // 결재테이블 JPA 양방향 관계 정립
                optionalApproval.get().getApprovalLogEntities().add( result );
                // 사원테이블 JPA 양방향 관계 정립
                optionalEmployee.get().getApprovalLogs().add( result );

                if( result.getAplogNo() < 1 ) return false;

            }
        }

        return false;

    }

    // 검토자 1명 승인
    @Transactional
    public boolean  approbate( int aprvNo, int aplogSta ) throws JsonProcessingException {

        // 추후 세션 호출 또는 userDetails 호출에 대한 구문기입 예정
            // 검토자
        Optional<EmployeeEntity> optionalEmployee = employeeRepository.findByEmpNo( "2311004" );
        Optional<ApprovalEntity> optionalApproval = approvalRepository.findById( aprvNo );
        

        if( !optionalApproval.isPresent() || !optionalEmployee.isPresent() ) return false;

        Optional<ApprovalLogEntity> optionalApprovallogEntity = approvalLogRepository.findByAprvNoAndEmpNo(
                optionalApproval.get(), optionalEmployee.get() );

        switch ( aplogSta ){
            // 결재
            case 1 : optionalApprovallogEntity.get().setAplogSta( 1 ); break;
            // 반려
            case 2 : optionalApprovallogEntity.get().setAplogSta( 2 ); break;
            default : return false;
        }

        // 해당 검토자가 최종 검토자일 경우
        if( approvalLogRepository.findMinAplogNoByAprvNo( optionalApproval.get() ) == optionalApprovallogEntity.get().getAplogNo() ){
            // 결재의 종류가 수정(put)일 경우
            // 기존 DB에 저장된 문자열JSON 이용하여 DB UPDATE 실행
            switch ( optionalApproval.get().getAprvType() ){

                // 사원정보 수정
                case 2: case 4: case 5:
                    return updateMemberInfoAproval( optionalApproval.get().getAprvNo() );

                // (휴직/연차/병가) 기간 변경 수정
                case 7: case 9: case 11:
                    return updateLeaveRequestInfoAproval( optionalApproval.get().getAprvNo() );

                // 프로젝트 수정
                case 13:
                    return updateProjectInfoAproval( optionalApproval.get().getAprvNo() );

                // 프로젝트 팀 사원 수정
                case 16:
                    return updateTeamMemberApproval( optionalApproval.get().getAprvNo() );

            }
        }

        return true;
    }

    // 결재 완료된 사원 '수정' 기능 메서드
    @Transactional
    public boolean updateMemberInfoAproval( int aprvNo ) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, true);

        Optional<ApprovalEntity> optionalApprovalEntity = approvalRepository.findById( aprvNo );
        if( !optionalApprovalEntity.isPresent() ) return false;

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
    public boolean updateLeaveRequestInfoAproval( int aprvNo ) throws JsonProcessingException {



        return false;
    }

    // 결재 완료된 프로젝트 '수정' 기능 메서드
    @Transactional
    public boolean updateProjectInfoAproval( int aprvNo ) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, true);

        Optional<ApprovalEntity> optionalApprovalEntity = approvalRepository.findById( aprvNo );
        if( !optionalApprovalEntity.isPresent() ) return false;

        // JSON문자열 => DTO객체로 변환
        ProjectDto projectDto
                = objectMapper.readValue(optionalApprovalEntity.get().getAprvJson(), ProjectDto.class);

        // DTO => Entity 객체로 변환
        ProjectEntity projectEntity = projectDto.saveToEntity();

        // 수정 전 해당 결재수정안에 대한 data 호출
            // 변경대상 프로젝트 호출
        Optional<ProjectEntity> optionalProjectEntity = projectRepository.findById( projectDto.getPjtNo() );

        if( optionalProjectEntity.isPresent() ){
            
            // 수정 PM ENTITY 호출
            Optional<EmployeeEntity> pmEntity = employeeRepository.findByEmpNo( projectDto.getEmpNo() );
            // 프로젝트 변경
            optionalProjectEntity.get().setEmpNo( pmEntity.get() );       // 프로젝트PM
            optionalProjectEntity.get().setPjtName( projectEntity.getPjtName() );   // 프로젝트명
            optionalProjectEntity.get().setPjtSt( projectEntity.getPjtSt() );       // 시작일자
            optionalProjectEntity.get().setPjtEND( projectEntity.getPjtEND() );     // 마감일자
            optionalProjectEntity.get().setPjtSta( projectEntity.getPjtSta() );     // 프로젝트 진행상태 ( 0:진행x 1:진행중 )

            projectRepository.save( optionalProjectEntity.get() );

        }

        return false;
    }

    // 결재 완료된 팀 멤버 '수정' 기능 메서드
    @Transactional
    public boolean updateTeamMemberApproval( int aprvNo ) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, true);

        Optional<ApprovalEntity> optionalApprovalEntity = approvalRepository.findById( aprvNo );
        if( !optionalApprovalEntity.isPresent() ) return false;

        // JSON문자열 => DTO객체로 변환
        TeamMemberDto teamMemberDto
                = objectMapper.readValue(optionalApprovalEntity.get().getAprvJson(), TeamMemberDto.class);

        // ※ DTO => Entity 객체로 변환할 생성자가 없으므로 생략

        // 수정 전 해당 결재수정안에 대한 data 호출
        // 변경대상 프로젝트 팀원 호출
        Optional<ProjectEntity> optionalProjectEntity = projectRepository.findById( teamMemberDto.getTmNo() );

        if( optionalProjectEntity.isPresent() ){

            // 수정 PM ENTITY 호출
            Optional<TeamMemberEntity> optionalTeamMemberEntity = teamMemberRepository.findById( teamMemberDto.getTmNo() );
            // 프로젝트 변경
            optionalTeamMemberEntity.get().setTmNo( teamMemberDto.getTmNo() );
            optionalTeamMemberEntity.get().setTmSt( teamMemberDto.getTmSt() );
            optionalTeamMemberEntity.get().setTmEnd( teamMemberDto.getTmEnd() );

            projectRepository.save( optionalProjectEntity.get() );

        }

        return false;
    }












}
