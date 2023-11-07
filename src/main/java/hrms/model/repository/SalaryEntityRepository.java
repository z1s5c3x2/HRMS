package hrms.model.repository;

import hrms.model.entity.LeaveRequestEntity;
import hrms.model.entity.SalaryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SalaryEntityRepository extends JpaRepository<SalaryEntity,Integer> {
    Optional<SalaryEntity> findByEmpNo( String empNo );
    Optional<SalaryEntity> findBySlryNo( int slryNo );
    // 새로운 메서드 추가
    List<SalaryEntity> findByEmpNoEmpNo(String empNo);
}
