package hrms.model.repository;

import hrms.model.entity.LeaveRequestEntity;
import hrms.model.entity.SalaryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SalaryEntityRepository extends JpaRepository<SalaryEntity,Integer> {
    Optional<SalaryEntity> findByEmpNo( String empNo );
    Optional<SalaryEntity> findBySlryNo( int slryNo );
    // 새로운 메서드 추가
    Page<SalaryEntity> findByEmpNo_EmpNo(String empNo, Pageable pageable);

    @Query( value = " select * from slry where " +
            " if( :keyword = '' , true , " +  // 전체검색  [ 조건1 ]
            " if( :key = 'empNo' , emp_no like %:keyword% , " + // [조건2]
            " if( :key = 'slryType' , slry_type like %:keyword% , true ) ) ) order by slry_date desc" // [조건3]
            , nativeQuery = true )
    Page<SalaryEntity> findBySearch(String key , String keyword , Pageable pageable );
}
