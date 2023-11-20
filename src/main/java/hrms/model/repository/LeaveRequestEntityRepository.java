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
public interface LeaveRequestEntityRepository extends JpaRepository<LeaveRequestEntity, Integer> {

    @Query(value = "SELECT TIMESTAMPDIFF(YEAR, (select cdate from emp where emp_no= :empNo), (date_add(now(),interval 1 YEAR ))) as ph\n" +
            "     ,(SELECT  sum(DATEDIFF(lrq_end,lrq_st)) AS rcount FROM lrq WHERE emp_no = :empNo and DATE_FORMAT(cdate, '%Y') = :nowYear) as cnt", nativeQuery = true)
    Map<Object, Integer> getRestCountByEmpNo(String empNo, String nowYear);



    @Query(value =
            "SELECT * " +
                    "FROM lrq " +
                    "WHERE" +
                    " emp_no = ?1 " +
                    "   AND (CASE WHEN ?2 > 0 THEN lrq_type = ?2 ELSE true END) " +
                    "   AND (CASE WHEN ?3 < 2 THEN lrq_srtype = ?3 ELSE true END) " +
                    "   AND (CASE WHEN ?4 IS NOT NULL AND ?5 IS NOT NULL THEN lrq_st >= CAST(?4 AS DATE) AND lrq_end <= CAST(?5 AS DATE) END OR " +
                    "        (CASE WHEN ?4 IS NOT NULL AND ?5 = '' THEN lrq_st >= CAST(?4 AS DATE) END) OR " +
                    "        (CASE WHEN ?4 = '' AND ?5 IS NOT NULL THEN lrq_end <= CAST(?5 AS DATE) END) OR " +
                    "        (CASE WHEN ?4 = '' AND ?5 = '' THEN true END)) " +
                    "ORDER BY l.lrq_st DESC", nativeQuery = true)
    Page<LeaveRequestEntity> findByEmpNo_EmpNo(String empNo , int lrqType, int lrqSrtype, String DateSt, String DateEnd , Pageable pageable);

    @Query(value = "SELECT l.*, e.emp_rk, d.dptm_no FROM lrq l " +
            "INNER JOIN EMP e ON l.emp_no = e.emp_no " +
            "INNER JOIN DPTM d ON e.dptm_no = d.dptm_no " +
            "WHERE" +
            "   (CASE WHEN ?1 = 'empNo' AND ?2 IS NOT NULL THEN e.emp_no LIKE CONCAT('%', ?2, '%') ELSE true END) " +
            "   AND (CASE WHEN ?3 > 0 THEN e.emp_rk = ?3 ELSE true END) " +
            "   AND (CASE WHEN ?4 > 0 THEN d.dptm_no = ?4 ELSE true END) " +
            "   AND (CASE WHEN ?5 > 0 THEN l.lrq_type = ?5 ELSE true END) " +
            "   AND (CASE WHEN ?6 < 2 THEN l.lrq_srtype = ?6 ELSE true END) " +
            "   AND (CASE WHEN ?7 IS NOT NULL AND ?8 IS NOT NULL THEN l.lrq_st >= CAST(?7 AS DATE) AND l.lrq_end <= CAST(?8 AS DATE) END OR " +
            "        (CASE WHEN ?7 IS NOT NULL AND ?8 = '' THEN l.lrq_st >= CAST(?7 AS DATE) END) OR " +
            "        (CASE WHEN ?7 = '' AND ?8 IS NOT NULL THEN l.lrq_end <= CAST(?8 AS DATE) END) OR " +
            "        (CASE WHEN ?7 = '' AND ?8 = '' THEN true END)) " +
            "ORDER BY l.lrq_st DESC", nativeQuery = true)
    Page<LeaveRequestEntity> findBySearch(String key, String keyword, int empRk, int dptmNo, int lrqType, int lrqSrtype, String DateSt, String DateEnd, Pageable pageable);

    LeaveRequestEntity findByAprvNo(ApprovalEntity aprvNo);
}
