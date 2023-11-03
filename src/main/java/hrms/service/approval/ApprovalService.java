package hrms.service.approval;

import hrms.model.dto.ApprovalRequestDto;
import hrms.model.dto.EmployeeDto;
import hrms.model.entity.ApprovalEntity;
import hrms.model.entity.ApprovalLogEntity;
import hrms.model.entity.EmployeeEntity;
import hrms.model.repository.ApprovalLogEntityRepository;
import hrms.model.repository.ApprovalEntityRepository;
import hrms.model.repository.DepartmentEntityRepository;
import hrms.model.repository.EmployeeEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ApprovalService {

    @Autowired
    private ApprovalEntityRepository approvalRepository;
    @Autowired
    private ApprovalLogEntityRepository approvalLogRepository;
    @Autowired
    private EmployeeEntityRepository employeeEntityRepository;

    // 결재 테이블 등록 [등록 기능에 관한 테이블]
    @Transactional
    public ApprovalEntity postApproval(int aprvType, String aprvCont, ArrayList<String> approvers ){
        
        // 추후 세션 호출 또는 userDetails 호출에 대한 구문기입 예정
        Optional<EmployeeEntity> optionalEmployeeEntity = employeeEntityRepository.findByEmpNo( "aaa" );

        ApprovalEntity approvalEntity = ApprovalEntity
                .builder()
                .aprvType(aprvType)
                .aprvCont(aprvCont)
                .empNo(optionalEmployeeEntity.get())
                .build();

        ApprovalEntity result = approvalRepository.save( approvalEntity );
        postApprovalLog( approvers, result.getAprvNo() );

        // 2.
        if( result.getAprvNo() >= 1 )  return result;
        return null;
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

                ApprovalLogEntity result = approvalLogRepository.save( logEntity );
                result.setAprvNo( optionalApproval.get() );

                /* 양방향 */
                    // 결재테이블
                optionalApproval.get().getApprovalLogEntities().add( result );
                optionalEmployee.get().getApprovalLogs().add( result );

                if( result.getAplogNo() < 1 ) return false;

            }
        }


        return false;

    }



}
