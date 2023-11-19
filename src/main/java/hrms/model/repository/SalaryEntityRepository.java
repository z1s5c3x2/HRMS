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


    @Query(value =
            "SELECT * " +
                    "FROM SLRY " +
                    "WHERE" +
                    " emp_no = ?1 " +
                    "   AND (CASE WHEN ?2 > 0 THEN slry_type = ?2 ELSE true END) " +
                    "   AND (CASE WHEN ?3 IS NOT NULL AND ?4 IS NOT NULL THEN slry_date BETWEEN CAST(?3 AS DATE) AND CAST(?4 AS DATE) END OR " +
                    "        (CASE WHEN ?3 IS NOT NULL AND ?4 = '' THEN slry_date >= CAST(?3 AS DATE) END) OR " +
                    "        (CASE WHEN ?3 ='' AND ?4 IS NOT NULL THEN slry_date <= CAST(?4 AS DATE) END) OR " +
                    "        (CASE WHEN ?3 = '' AND ?4 = '' THEN true END)) " +
                    "ORDER BY slry_date DESC", nativeQuery = true)
    Page<SalaryEntity> findByEmpNo_EmpNo(String empNo, int slryType, String DateSt, String DateEnd, Pageable pageable);

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
                    "   AND (CASE WHEN ?6 IS NOT NULL AND ?7 IS NOT NULL THEN s.slry_date BETWEEN CAST(?6 AS DATE) AND CAST(?7 AS DATE) END OR " +
                    "        (CASE WHEN ?6 IS NOT NULL AND ?7 = '' THEN s.slry_date >= CAST(?6 AS DATE) END) OR " +
                    "        (CASE WHEN ?6 ='' AND ?7 IS NOT NULL THEN s.slry_date <= CAST(?7 AS DATE) END) OR " +
                    "        (CASE WHEN ?6 = '' AND ?7 = '' THEN true END)) " +
                    "ORDER BY s.slry_date DESC", nativeQuery = true)
    Page<SalaryEntity> findBySearch(String key, String keyword, int empRk, int dptmNo, int slryType, String DateSt, String DateEnd, Pageable pageable);
}