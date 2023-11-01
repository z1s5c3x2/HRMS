package hrms.model.repository;

import hrms.model.entity.SalaryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalaryEntityRepository extends JpaRepository<SalaryEntity,Integer> {
}
