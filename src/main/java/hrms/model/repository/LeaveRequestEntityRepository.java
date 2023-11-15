package hrms.model.repository;

import hrms.model.entity.ApprovalEntity;
import hrms.model.entity.LeaveRequestEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface LeaveRequestEntityRepository extends JpaRepository<LeaveRequestEntity,Integer> {

    @Query(value = "SELECT TIMESTAMPDIFF(YEAR, (select cdate from emp where emp_no= :empNo), (date_add(now(),interval 1 YEAR ))) as ph\n" +
            "     ,(SELECT  COUNT(emp_no) AS rcount FROM lrq WHERE emp_no = :empNo and DATE_FORMAT(cdate, '%Y') = :nowYear) as cnt",nativeQuery = true)
    Map<Object,Integer> getRestCountByEmpNo(String empNo, String nowYear);

    Page<LeaveRequestEntity> findByEmpNo_EmpNo(String empNo, Pageable pageable);

    @Query( value = " select * from lrq where " +
            " if( :keyword = '' , true , " +  // 전체검색  [ 조건1 ]
            " if( :key = 'empNo' , emp_no like %:keyword% , " + // [조건2]
            " if( :key = 'lrqType' , lrq_type like %:keyword% , true ) ) ) order by lrq_st desc" // [조건3]
            , nativeQuery = true )
    Page<LeaveRequestEntity> findBySearch(String key , String keyword , Pageable pageable );

    LeaveRequestEntity findByAprvNo(ApprovalEntity aprvNo);
}
