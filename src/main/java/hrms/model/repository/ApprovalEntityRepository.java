package hrms.model.repository;

import hrms.model.entity.ApprovalEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface ApprovalEntityRepository extends JpaRepository<ApprovalEntity,Integer> {

    @Query( value = "select * from APRV where emp_no=:empNo" , nativeQuery = true )
    List<ApprovalEntity> findByAllempNo( String empNo );


    // 결재건 출력 필터 검색
    @Query( value = "SELECT aprv.* FROM aprv JOIN aplog ON aprv.aprv_no = aplog.aprv_no JOIN emp ON aprv.emp_no = emp.emp_no " +
            "WHERE " +
                // 필터 : key 검색
            "   ( CASE" +
                    // 검색 key( 결재번호 / 내용 / 상신자 )가 없을 경우 전체 출력
            "       WHEN :key = '' THEN TRUE " +
                    // key에 따른 조건 출력
            "       ELSE "+
            "           CASE "+
            "               WHEN :key = 'aprv_cont' THEN aprv.aprv_cont LIKE CONCAT('%', :keyword, '%') "+
            "               WHEN :key = 'aprv_no' THEN aprv.aprv_no LIKE CONCAT('%', :keyword, '%') "+
            "               WHEN :key = 'emp_name' THEN emp.emp_name LIKE CONCAT('%', :keyword, '%') "+
            "               END " +
            "       END " +
            "   ) " +
            "AND " +
                // 필터 : 일자 검색
            "   ( case " +
                    // 시작일자 혹은 종료일자가 입력되지 않은 경우 전체 조회
            "       WHEN :strDate = '' OR :endDate = '' THEN aprv.cdate BETWEEN (SELECT MIN(cdate) FROM aprv ) AND now() " +
                    //	종료일자는 하루 추가하여 계산 (시스템상 당일에 대한 cdate는 해당되지 않음)
            "       ELSE aprv.cdate BETWEEN :strDate AND DATE_ADD(:endDate, INTERVAL 1 DAY) " +
            "       END" +
            "   ) " +
            "AND " +
                // 필터 : 결재 진행현황 검색
            "   (case " +
            "       WHEN :apState = 0 THEN true " +
                    // 완료된 결재건
            "       WHEN :apState = 1 THEN (SELECT COUNT(*) FROM aplog WHERE aprv.aprv_no = aplog.aprv_no AND aplog.aplog_sta = 1) = (SELECT COUNT(*) FROM aplog WHERE aprv.aprv_no = aplog.aprv_no) " +
                    // 결재 반려건
            "       WHEN :apState = 2 THEN (SELECT COUNT(*) FROM aplog WHERE aprv.aprv_no = aplog.aprv_no AND aplog.aplog_sta = 2) > 0 " +
                    // 결재 진행건
            "       WHEN :apState = 3 THEN (SELECT COUNT(*) FROM aplog WHERE aprv.aprv_no = aplog.aprv_no AND aplog.aplog_sta = 3) = (SELECT COUNT(*) FROM aplog WHERE aprv.aprv_no = aplog.aprv_no) " +
            "       end " +
            "   ) GROUP BY aprv.aprv_no, aplog.aprv_no ORDER BY cdate DESC"
            , nativeQuery = true )
    Page< ApprovalEntity > findBySearch( String key, String keyword, int apState, String strDate, String endDate, Pageable pageable );

}
