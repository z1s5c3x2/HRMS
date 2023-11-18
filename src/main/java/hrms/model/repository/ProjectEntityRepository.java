package hrms.model.repository;

import hrms.model.entity.ProjectEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectEntityRepository extends JpaRepository<ProjectEntity,Integer> {

    @Query(value = "SELECT * FROM PJT_MNG p " +
            "WHERE NOT EXISTS (" +
            "    SELECT * FROM APLOG a " +
            "    WHERE a.aprv_no = p.aprv_no " +
            "    AND a.aplog_sta != 1" +
            ")",
            countQuery = "SELECT count(*) FROM PJT_MNG p " +
                    "WHERE NOT EXISTS (" +
                    "    SELECT * FROM APLOG a " +
                    "    WHERE a.aprv_no = p.aprv_no " +
                    "    AND a.aplog_sta != 1" +
                    ")",
            nativeQuery = true)
    Page<ProjectEntity> findByAllAplogStaIs1(Pageable pageable);

    @Query(value = "SELECT * FROM PJT_MNG p " +
            "WHERE EXISTS (SELECT 1 FROM APLOG a WHERE a.aprv_no = p.aprv_no AND a.aplog_sta = 2)",
            nativeQuery = true)
    Page<ProjectEntity> findProjectsByApprovalLogAplogSta2(Pageable pageable);

    @Query(value =
            "SELECT * FROM PJT_MNG p " +
                    "WHERE NOT EXISTS (" +
                    "    SELECT 1 FROM APLOG a " +
                    "    WHERE a.aprv_no = p.aprv_no AND a.aplog_sta = 2) " +
                    "AND EXISTS (" +
                    "    SELECT 1 FROM APLOG a " +
                    "    WHERE a.aprv_no = p.aprv_no AND a.aplog_sta = 3) ",
            countQuery = "SELECT COUNT(*) FROM PJT_MNG p " +
                    "WHERE NOT EXISTS (" +
                    "    SELECT 1 FROM APLOG a " +
                    "    WHERE a.aprv_no = p.aprv_no AND a.aplog_sta = 2) " +
                    "AND EXISTS (" +
                    "    SELECT 1 FROM APLOG a " +
                    "    WHERE a.aprv_no = p.aprv_no AND a.aplog_sta = 3)",
            nativeQuery = true)
    Page<ProjectEntity> findProjectsByApprovalLog(Pageable pageable);
}
