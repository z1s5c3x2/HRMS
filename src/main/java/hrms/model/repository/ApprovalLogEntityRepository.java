package hrms.model.repository;

import hrms.model.entity.ApprovalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApprovalLogEntityRepository extends JpaRepository<ApprovalEntity,Integer> {
}
