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


    // 전 사원결재 조회 출력 필터 검색
    @Query( value = "SELECT aprv.* FROM aprv JOIN aplog ON aprv.aprv_no = aplog.aprv_no JOIN emp ON aprv.emp_no = emp.emp_no " +
            "WHERE " +
                // 필터 : key 검색
            "   ( CASE " +
                    // 검색 key( 결재번호 / 내용 / 상신자 )가 없을 경우 전체 출력
            "       WHEN :keyword = '' THEN TRUE " +
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
            "       WHEN :apState = 3 THEN   " +
                        // 테이블에 '반려'(2) 가 하나도 존재하지 않는 조건
            "           NOT EXISTS " +
            "                   ( SELECT 1 FROM aplog AS inner_aplog_2 " +
            "                   WHERE " +
            "                       inner_aplog_2.aprv_no = aprv.aprv_no " +
            "                   AND " +
            "                       inner_aplog_2.aplog_sta = 2) " +
            "           AND " +
                        // 테이블에 '검토중'(3) 이 하나라도 존재하는 조건
            "           EXISTS  " +
            "                   ( SELECT 1 FROM aplog AS inner_aplog_3 " +
            "                   WHERE " +
            "                       inner_aplog_3.aprv_no = aprv.aprv_no " +
            "                   AND inner_aplog_3.aplog_sta = 3 ) " +
            "       end " +
            "   ) GROUP BY aplog.aprv_no ORDER BY cdate DESC limit :startRow, :maxSize"
            , nativeQuery = true )
    List< ApprovalEntity > findBySearch( String key, String keyword, int apState, String strDate, String endDate, int startRow, int maxSize );

    // 피결재목록 출력 필터 검색
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
            "   ( CASE " +
                    // 기본값
            "       when :apState = 0 THEN ( " +
                        // 현재 검토자가 아닌 경우 제외 후 전체 조회
            "           NOT EXISTS ( " +
            "               SELECT 1 FROM aplog AS prev_aplog " +
            "               WHERE " +
                                // 다수의 이전 검토자와 현 검토자의 결재번호를 매핑
            "                   prev_aplog.aprv_no = aplog.aprv_no " +
            "               AND " +
                                // 다수의 이전 검토자를 지목
            "                   prev_aplog.aplog_no < aplog.aplog_no " +
                        // 완료 여부 확인
            "           AND prev_aplog.aplog_sta != 1 " +
            "           ) " +
            "       ) " +
                    // 완료된 결재건
            "       when :apState = 1 THEN aplog_sta = 1 " +
                    // 결재 반려건
            "       when :apState = 2 THEN aplog_sta = 2 " +
                    // 결재 진행건
            "       when :apState = 3 THEN( " +
                        // 최초검토자이고 aplog_sta가 3인 경우
            "           aplog.aplog_no = ( " +
            "               SELECT MIN(aplog_no) FROM aplog " +
            "               WHERE " +
            "                   aprv.aprv_no = aplog.aprv_no ) " +
            "               AND " +
            "                   aplog.aplog_sta = 3" +
            "       ) " +
            "       OR" +
            "       (" +
                        // 이전 검토자들이 모두 '완료'한 경우
            "           NOT EXISTS (" +
            "               SELECT 1 FROM aplog AS prev_aplog " +
            "                   WHERE " +
                                    // 다수의 이전 검토자와 현 검토자의 동일한 결재번호를 매핑
            "                       prev_aplog.aprv_no = aplog.aprv_no " +
            "                   AND " +
                                    // 다수의 이전 검토자를 지목
            "                       prev_aplog.aplog_no < aplog.aplog_no " +
            "                   AND " +
                                    //  완료 여부 확인
            "                       prev_aplog.aplog_sta != 1 " +
            "           )" +
            "           AND aplog.aplog_sta = 3 " +
            "           )" +
            "           end" +
            ") " +
            "AND " +
            "aplog.emp_no = :empNo GROUP BY aprv.aprv_no, aplog.aprv_no ORDER BY cdate DESC " +
            "LIMIT :startRow, :maxSize"
            , nativeQuery = true )
    List< ApprovalEntity > approvalViewSearch( String key, String keyword, int apState, String strDate, String endDate, String empNo, int startRow, int maxSize );

    // 개별 상신 출력 필터 검색
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
            "       WHEN :apState = 3 THEN   " +
                        // 테이블에 '반려'(2) 가 하나도 존재하지 않는 조건
            "           NOT EXISTS " +
            "                   ( SELECT 1 FROM aplog AS inner_aplog_2 " +
            "                   WHERE " +
            "                       inner_aplog_2.aprv_no = aprv.aprv_no " +
            "                   AND " +
            "                       inner_aplog_2.aplog_sta = 2) " +
            "           AND " +
                        // 테이블에 '검토중'(3) 이 하나라도 존재하는 조건
            "           EXISTS  " +
            "                   ( SELECT 1 FROM aplog AS inner_aplog_3 " +
            "                   WHERE " +
            "                       inner_aplog_3.aprv_no = aprv.aprv_no " +
            "                   AND inner_aplog_3.aplog_sta = 3 ) " +
            "       end " +
            ") " +
            "AND " +
            "aprv.emp_no = :empNo GROUP BY aprv.aprv_no, aplog.aprv_no ORDER BY cdate DESC " +
            "LIMIT :startRow, :maxSize "
            , nativeQuery = true )
    List< ApprovalEntity > reconsiderViewSearch( String key, String keyword, int apState, String strDate, String endDate, String empNo, int startRow, int maxSize );


    /* 레코드 수 산출 ========================================================================= */

    // 전 사원결재 조회 출력 사이즈 검색
    @Query( value = "SELECT aprv.* FROM aprv JOIN aplog ON aprv.aprv_no = aplog.aprv_no JOIN emp ON aprv.emp_no = emp.emp_no " +
            "WHERE " +
            // 필터 : key 검색
            "   ( CASE " +
            // 검색 key( 결재번호 / 내용 / 상신자 )가 없을 경우 전체 출력
            "       WHEN :keyword = '' THEN TRUE " +
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
            "       WHEN :apState = 3 THEN   " +
            // 테이블에 '반려'(2) 가 하나도 존재하지 않는 조건
            "           NOT EXISTS " +
            "                   ( SELECT 1 FROM aplog AS inner_aplog_2 " +
            "                   WHERE " +
            "                       inner_aplog_2.aprv_no = aprv.aprv_no " +
            "                   AND " +
            "                       inner_aplog_2.aplog_sta = 2) " +
            "           AND " +
            // 테이블에 '검토중'(3) 이 하나라도 존재하는 조건
            "           EXISTS  " +
            "                   ( SELECT 1 FROM aplog AS inner_aplog_3 " +
            "                   WHERE " +
            "                       inner_aplog_3.aprv_no = aprv.aprv_no " +
            "                   AND inner_aplog_3.aplog_sta = 3 ) " +
            "       end " +
            "   ) GROUP BY aplog.aprv_no ORDER BY cdate DESC"
            , nativeQuery = true )
    List< ApprovalEntity > findByMaxCheck( String key, String keyword, int apState, String strDate, String endDate );

    // 피결재목록 출력 사이즈 검색
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
            "   ( CASE " +
            // 기본값
            "       when :apState = 0 THEN ( " +
            // 현재 검토자가 아닌 경우 제외 후 전체 조회
            "           NOT EXISTS ( " +
            "               SELECT 1 FROM aplog AS prev_aplog " +
            "               WHERE " +
            // 다수의 이전 검토자와 현 검토자의 결재번호를 매핑
            "                   prev_aplog.aprv_no = aplog.aprv_no " +
            "               AND " +
            // 다수의 이전 검토자를 지목
            "                   prev_aplog.aplog_no < aplog.aplog_no " +
            // 완료 여부 확인
            "           AND prev_aplog.aplog_sta != 1 " +
            "           ) " +
            "       ) " +
            // 완료된 결재건
            "       when :apState = 1 THEN aplog_sta = 1 " +
            // 결재 반려건
            "       when :apState = 2 THEN aplog_sta = 2 " +
            // 결재 진행건
            "       when :apState = 3 THEN( " +
            // 최초검토자이고 aplog_sta가 3인 경우
            "           aplog.aplog_no = ( " +
            "               SELECT MIN(aplog_no) FROM aplog " +
            "               WHERE " +
            "                   aprv.aprv_no = aplog.aprv_no ) " +
            "               AND " +
            "                   aplog.aplog_sta = 3" +
            "       ) " +
            "       OR" +
            "       (" +
            // 이전 검토자들이 모두 '완료'한 경우
            "           NOT EXISTS (" +
            "               SELECT 1 FROM aplog AS prev_aplog " +
            "                   WHERE " +
            // 다수의 이전 검토자와 현 검토자의 동일한 결재번호를 매핑
            "                       prev_aplog.aprv_no = aplog.aprv_no " +
            "                   AND " +
            // 다수의 이전 검토자를 지목
            "                       prev_aplog.aplog_no < aplog.aplog_no " +
            "                   AND " +
            //  완료 여부 확인
            "                       prev_aplog.aplog_sta != 1 " +
            "           )" +
            "           AND aplog.aplog_sta = 3 " +
            "           )" +
            "           end" +
            ") " +
            "AND " +
            "aplog.emp_no = :empNo GROUP BY aprv.aprv_no, aplog.aprv_no ORDER BY cdate DESC "
            , nativeQuery = true )
    List< ApprovalEntity > approvalViewMaxCheck( String key, String keyword, int apState, String strDate, String endDate, String empNo );

    // 개별 상신 출력 사이즈 검색
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
            "       WHEN :apState = 3 THEN   " +
            // 테이블에 '반려'(2) 가 하나도 존재하지 않는 조건
            "           NOT EXISTS " +
            "                   ( SELECT 1 FROM aplog AS inner_aplog_2 " +
            "                   WHERE " +
            "                       inner_aplog_2.aprv_no = aprv.aprv_no " +
            "                   AND " +
            "                       inner_aplog_2.aplog_sta = 2) " +
            "           AND " +
            // 테이블에 '검토중'(3) 이 하나라도 존재하는 조건
            "           EXISTS  " +
            "                   ( SELECT 1 FROM aplog AS inner_aplog_3 " +
            "                   WHERE " +
            "                       inner_aplog_3.aprv_no = aprv.aprv_no " +
            "                   AND inner_aplog_3.aplog_sta = 3 ) " +
            "       end " +
            ") " +
            "AND " +
            "aprv.emp_no = :empNo GROUP BY aprv.aprv_no, aplog.aprv_no ORDER BY cdate DESC "
            , nativeQuery = true )
    List< ApprovalEntity > reconsiderMaxCheck( String key, String keyword, int apState, String strDate, String endDate, String empNo );













}



























