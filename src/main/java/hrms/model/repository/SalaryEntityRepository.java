package hrms.model.repository;

import hrms.model.entity.SalaryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SalaryEntityRepository extends JpaRepository<SalaryEntity, Integer> {
    Optional<SalaryEntity> findByEmpNo(String empNo);

    Optional<SalaryEntity> findBySlryNo(int slryNo);

    // 새로운 메서드 추가
    Page<SalaryEntity> findByEmpNo_EmpNo(String empNo, Pageable pageable);

    @Query(value =
            "SELECT s.*, e.emp_rk, d.dptm_no " +
                    "FROM SLRY s " +
                    "INNER JOIN EMP e ON s.emp_no = e.emp_no " +
                    "INNER JOIN DPTM d ON e.dptm_no = d.dptm_no " +
                    "WHERE" +
                    "   (CASE WHEN ?1 = 'empNo' AND ?2 IS NOT NULL THEN e.emp_no LIKE CONCAT('%', ?2, '%') ELSE true END) " +
                    "   AND (CASE WHEN ?3 > 0 THEN e.emp_rk = ?3 ELSE true END) " +
                    "   AND (CASE WHEN ?4 > 0 THEN d.dptm_no = ?4 ELSE true END) " +
                    "   AND (CASE WHEN ?5 > 0 THEN s.slry_type = ?5 ELSE true END) " +
                    "ORDER BY s.slry_date DESC", nativeQuery = true)
    Page<SalaryEntity> findBySearch(String key, String keyword, int empRk, int dptmNo, int slryType, Pageable pageable);
}