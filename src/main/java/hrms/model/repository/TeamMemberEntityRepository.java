package hrms.model.repository;

import hrms.model.entity.EmployeeEntity;
import hrms.model.entity.TeamMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamMemberEntityRepository extends JpaRepository<TeamMemberEntity,Integer> {
    Optional<TeamMemberEntity> findTop1ByEmpNoAndTmEndIsNullOrderByTmNoDesc(EmployeeEntity empNo);
}
