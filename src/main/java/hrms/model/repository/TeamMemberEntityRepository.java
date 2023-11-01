package hrms.model.repository;

import hrms.model.entity.TeamMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamMemberEntityRepository extends JpaRepository<TeamMemberEntity,Integer> {
}
