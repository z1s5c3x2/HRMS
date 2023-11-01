package hrms.model.repository;

import hrms.model.entity.LeaveRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LeaveRequestEntityRepository extends JpaRepository<LeaveRequestEntity,Integer> {
    Optional<LeaveRequestEntity> findByEmpNo(int empNo );
}
