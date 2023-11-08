package hrms.model.repository;

import hrms.model.entity.DepartmentEntity;
import hrms.model.entity.EmployeeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeEntityRepository extends JpaRepository<EmployeeEntity,Integer> {
    @Query(value = "SELECT  COUNT(emp_no) AS emp_count\n" +
            "FROM emp\n" +
            "WHERE DATE_FORMAT(cdate, '%Y') = :nowYear\n"
            ,nativeQuery = true)
    int countNowEmployee(String nowYear);
    List<EmployeeEntity> findAllByEmpStaIsFalse();
    Optional<EmployeeEntity> findByEmpNo(String empNo);
    List<EmployeeEntity> findByDptmNoOrderByEmpRkDesc(DepartmentEntity dptmNo);
    @Query(value = "select *\n" +
            "from emp\n" +
            "where IF( :pageSta = 0, true,\n" +
            "         IF( :pageSta = 1, emp_sta, not emp_sta))\n" +
            "  and IF( :dptmNo = 0, true,\n" +
            "         :dptmNo = dptm_no)",nativeQuery = true)
    Page<EmployeeEntity> findByEmpPage(int pageSta, int dptmNo,Pageable pageable);

}
