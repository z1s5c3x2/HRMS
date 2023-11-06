package hrms.model.repository;

import hrms.model.entity.LeaveRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;

@Repository
public interface LeaveRequestEntityRepository extends JpaRepository<LeaveRequestEntity,Integer> {
    Optional<LeaveRequestEntity> findByEmpNo(String empNo );
    @Query(value = "SELECT TIMESTAMPDIFF(YEAR, (select cdate from emp where emp_no= :empNo), (date_add(now(),interval 1 YEAR ))) as ph\n" +
            "     ,(SELECT  COUNT(emp_no) AS rcount FROM lrq WHERE emp_no = :empNo and DATE_FORMAT(cdate, '%Y') = :nowYear) as cnt",nativeQuery = true)
    Map<Object,Integer> getRestCountByEmpNo(String empNo, String nowYear);

}
