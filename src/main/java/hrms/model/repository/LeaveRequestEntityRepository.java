package hrms.model.repository;

import hrms.model.entity.LeaveRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaveRequestEntityRepository extends JpaRepository<LeaveRequestEntity,Integer> {
}
