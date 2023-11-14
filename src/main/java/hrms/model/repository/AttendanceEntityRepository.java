package hrms.model.repository;

import hrms.model.entity.AttendanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceEntityRepository extends JpaRepository<AttendanceEntity,Integer> {


}
