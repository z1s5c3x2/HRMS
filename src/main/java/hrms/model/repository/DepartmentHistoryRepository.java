package hrms.model.repository;

import hrms.model.entity.DepartmentHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentHistoryRepository extends JpaRepository<DepartmentHistoryEntity,Integer> {
}
