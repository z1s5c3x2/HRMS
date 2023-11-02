package hrms.service.approval;

import hrms.model.dto.ApprovalRequestDto;
import hrms.model.dto.EmployeeDto;
import hrms.model.entity.ApprovalEntity;
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
    public Integer postApproval(int aprvType, String aprvCont, ArrayList<String> approvers ){
        
        // 추후 세션 호출 또는 userDetails 호출에 대한 구문기입 예정
        Optional<EmployeeEntity> optionalEmployeeEntity = employeeEntityRepository.findByEmpNo( "aaa" );

        ApprovalEntity approvalEntity = ApprovalEntity
                .builder()
                .aprvType(aprvType)
                .aprvCont(aprvCont)
                .empNo(optionalEmployeeEntity.get())
                .build();

        // int aprv_no, String aprv_cont, int emp_no
        ApprovalEntity result = approvalRepository.save( approvalEntity );

        // 검토자들의 EmployeeEntity 리스트 가져오기
        ArrayList<EmployeeEntity> approverEntities = employeeEntityRepository.findByEmpNoIn(approvers);

        postApprovalLog( approvers, result.getAprvNo() );

        // 2.
        if( result.getAprvNo() >= 1 )  return result.getAprvNo();
        return 0;
    }

    // 결재 테이블 내역 검토자 등록
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    public boolean postApprovalLog( ArrayList<String> approvers, int aprvNo ) {

        String sql = "INSERT INTO aplog (emp_no, aprv_no) VALUES ";

        for (int i = 0; i < approvers.size(); i++) {
            if (i > 0) sql += ", ";
            sql += "(\'" + approvers.get(i) + "\', " + aprvNo + ")";
        }

        int updateCount = jdbcTemplate.update(sql);

        if (updateCount > 0)  return true;
        return false;
    }



}
