package hrms.service;

import hrms.model.entity.ApprovalEntity;
import hrms.model.entity.EmployeeEntity;
import hrms.model.repository.ApprovalLogRepository;
import hrms.model.repository.ApprovalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class Approval {

    @Autowired
    private ApprovalRepository approvalRepository;
    @Autowired
    private ApprovalLogRepository approvalLogRepository;

    // 결재 테이블 등록
    @Transactional
    public Integer postApproval( ApprovalEntity approvalEntity, ArrayList<EmployeeEntity> approvers ){

        // int aprv_no, String aprv_cont, int emp_no
        ApprovalEntity result = approvalRepository.save( approvalEntity );

        // 2.
        if( result.getAprv_no() >= 1 ){



            return result.getAprv_no();
        }

        return 0;
    }

    // 결재 테이블 내역 검토자 등록

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    public boolean postApprovalLog(ArrayList<EmployeeEntity> approvers) {
        String insertQuery = "INSERT INTO your_table (column1, column2, ...) VALUES (?, ?, ...)";

        List<Object[]> batchArgs = new ArrayList<>();

        for (EmployeeEntity approver : approvers) {
            // 각 레코드에 대한 데이터를 배열로 추가
            Object[] values = {approver.getField1(), approver.getField2(), /* 나머지 필드 값 */};
            batchArgs.add(values);
        }

        int[] updateCounts = jdbcTemplate.batchUpdate(insertQuery, batchArgs, batchSize);

        // 모든 작업이 성공했는지 확인
        for (int updateCount : updateCounts) {
            if (updateCount == 0) {
                return false; // 실패 시 false 반환
            }
        }

        return true; // 성공 시 true 반환
    }


}
