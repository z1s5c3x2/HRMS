package hrms.model.repository;

import hrms.model.entity.ApprovalEntity;
import hrms.model.entity.ApprovalLogEntity;
import hrms.model.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApprovalLogEntityRepository extends JpaRepository<ApprovalLogEntity,Integer> {

    // 1명의 검토자에 대한 판단결과(결재 반려 검토중)를 저장하기 위한 메소드
    Optional<ApprovalLogEntity> findByAprvNoAndEmpNo(ApprovalEntity approvalEntity, EmployeeEntity employeeEntity);
    List<ApprovalLogEntity> findByEmpNo(EmployeeEntity employeeEntity );

    // MIN(aplog_no)를 반환하는 쿼리 메서드 정의
    @Query("SELECT MIN(a.aplogNo) FROM ApprovalLogEntity a WHERE a.aprvNo = ?1")
    Integer findMinAplogNoByAprvNo(ApprovalEntity aprvNo);

}
