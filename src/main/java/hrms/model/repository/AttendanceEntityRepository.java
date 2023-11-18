package hrms.model.repository;

import hrms.model.entity.AttendanceEntity;
import hrms.model.entity.EmployeeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceEntityRepository extends JpaRepository<AttendanceEntity,Integer> {
    // 전사원 근무 상태 조회
    Page<AttendanceEntity> findAllByAttdWrstBetween(String start,String end, Pageable pageable);
    List<AttendanceEntity> findAllByEmpNoAndAttdWrstBetween(EmployeeEntity empNo, String start, String end);

}
