package hrms.model.repository;

import hrms.model.entity.DepartmentHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentHistoryEntityRepository extends JpaRepository<DepartmentHistoryEntity,Integer> {
}
