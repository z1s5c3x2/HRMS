package hrms.model.repository;

import hrms.model.entity.ApprovalEntity;
import hrms.model.entity.ApprovalLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApprovalLogEntityRepository extends JpaRepository<ApprovalLogEntity,Integer> {
}
