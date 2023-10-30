package hrms.model.repository;

import hrms.model.entity.EmployeeEntity;
import hrms.model.entity.RetiredEmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RetiredEmployeeRepository extends JpaRepository<RetiredEmployeeEntity,Integer> {
}
