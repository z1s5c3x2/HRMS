package hrms.service;

import hrms.model.entity.ApprovalEntity;
import hrms.model.entity.EmployeeEntity;
import hrms.model.repository.ApprovalLogEntityRepository;
import hrms.model.repository.ApprovalEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;

@Service
public class Approval {

    @Autowired
    private ApprovalEntityRepository approvalRepository;
    @Autowired
    private ApprovalLogEntityRepository approvalLogRepository;

    // 결재 테이블 등록
    @Transactional
    public Integer postApproval( ApprovalEntity approvalEntity, ArrayList<EmployeeEntity> approvers ){

        // int aprv_no, String aprv_cont, int emp_no
        ApprovalEntity result = approvalRepository.save( approvalEntity );
        postApprovalLog( approvers, result.getAprvNo() );

        // 2.
        if( result.getAprvNo() >= 1 )  return result.getAprvNo();
        return 0;
    }

    // 결재 테이블 내역 검토자 등록
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    public boolean postApprovalLog(ArrayList<EmployeeEntity> approvers, int aprvNo ) {

        String sql = "INSERT INTO your_table (emp_no, aprv_no) VALUES ";

        for (int i = 0; i < approvers.size(); i++) {
            if (i > 0) {
                sql += ", ";
            }
            sql += "(" + approvers.get(i).getEmpNo() + ", " + aprvNo + ")";
        }

        int updateCount = jdbcTemplate.update(sql);
        if (updateCount > 0)  return true;
        return false;
    }



}
