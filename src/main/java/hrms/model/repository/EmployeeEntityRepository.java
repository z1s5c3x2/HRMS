package hrms.model.repository;

import hrms.model.entity.DepartmentEntity;
import hrms.model.entity.EmployeeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeEntityRepository extends JpaRepository<EmployeeEntity,Integer> {
    @Query(value = "select count(aprv_no) as emp_count from aprv where DATE_FORMAT(cdate,'%Y') = :nowYear and aprv_type = 1"
            ,nativeQuery = true)
    int countNowEmployee(String nowYear); // 해당 해에 입사한 사원 수
    List<EmployeeEntity> findAllByEmpStaIsFalse(); // 근무 상태가 false사원 모두 찾기
    Optional<EmployeeEntity> findByEmpNo(String empNo); // 사원 개별 조회
    List<EmployeeEntity> findByDptmNoOrderByEmpRkDesc(DepartmentEntity dptmNo); // 부서내의 사원을 직급 순으로 조회
    @Query(value = "select *\n" +
            "from emp\n" +
            "where IF( :pageSta = 0, true,\n" +
            "         IF( :pageSta = 1, emp_sta, not emp_sta))\n" +
            "  and IF( :dptmNo = 0, true,\n" +
            "         :dptmNo = dptm_no)",nativeQuery = true)
    Page<EmployeeEntity> findByEmpPage(int pageSta, int dptmNo,Pageable pageable);   // 부서와 사원의 근무 상태 필터를 기준으로 출력
    @Query(value = "select * from emp where IF( :option = '0', emp_no like %:searchValue% ,emp_name like %:searchValue%)",nativeQuery = true)
    Page<EmployeeEntity> searchToOption(String option,String searchValue,Pageable pageable);

    /*@Query(value = "SELECT COUNT(*) AS row_count\n" +
            "FROM aprv AS a\n" +
            "WHERE emp_no = :empNo\n" +
            "  AND (\n" +
            "          SELECT COUNT(aprv_no) =0\n" +
            "          FROM aplog\n" +
            "          WHERE a.aprv_no = aplog.aprv_no AND aplog_sta = 3\n" +
            "      ) = false", nativeQuery = true)
    int findAprvCount(String empNo);*/
}
