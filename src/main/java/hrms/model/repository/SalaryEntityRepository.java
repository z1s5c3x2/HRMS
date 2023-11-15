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
                    "                    FROM SLRY s " +
                    "                    INNER JOIN EMP e ON s.emp_no = e.emp_no " +
                    "                    INNER JOIN DPTM d ON e.dptm_no = d.dptm_no " +
                    "                    WHERE" +
                    "   (:key = 'empNo' AND :keyword IS NOT NULL AND e.empNo LIKE CONCAT('%', :keyword, '%')) " +
                    "   AND (:empRk > 0 AND e.empRk = :empRk) " +
                    "   AND (:dptmNo > 0 AND d.dptmNo = :dptmNo) " +
                    "   AND (:slryType > 0 AND s.slryType = :slryType) " +
                    "ORDER BY s.slryDate DESC", nativeQuery = true)
    Page<SalaryEntity> findBySearch(String key, String keyword, int empRk, int dptmNo, int slryType, Pageable pageable);
}
