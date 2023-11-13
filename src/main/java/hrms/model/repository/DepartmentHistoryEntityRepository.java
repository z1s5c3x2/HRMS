package hrms.model.repository;

import hrms.model.entity.DepartmentHistoryEntity;
import hrms.model.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentHistoryEntityRepository extends JpaRepository<DepartmentHistoryEntity,Integer> {
    Optional<DepartmentHistoryEntity> findTop1ByEmpNoAndHdptmEndIsNullOrderByHdptmEndDesc(EmployeeEntity empNo);

}
