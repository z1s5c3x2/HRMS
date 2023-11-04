package hrms.service.approval;

import com.fasterxml.jackson.databind.ObjectMapper;
import hrms.model.entity.ApprovalEntity;
import hrms.model.entity.ApprovalLogEntity;
import hrms.model.entity.EmployeeEntity;
import hrms.model.repository.ApprovalLogEntityRepository;
import hrms.model.repository.ApprovalEntityRepository;
import hrms.model.repository.EmployeeEntityRepository;
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
    private EmployeeEntityRepository employeeEntityRepository;

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
        Optional<EmployeeEntity> optionalEmployeeEntity = employeeEntityRepository.findByEmpNo( "2311005" );

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
        Optional<EmployeeEntity> optionalEmployeeEntity = employeeEntityRepository.findByEmpNo( "2311005" );

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


    // 결재 테이블 내역 검토자 등록
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
            optionalEmployee = employeeEntityRepository.findByEmpNo( approver );

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

























}
