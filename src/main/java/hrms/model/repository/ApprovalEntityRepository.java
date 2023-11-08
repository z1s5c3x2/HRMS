package hrms.model.repository;

import hrms.model.entity.ApprovalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface ApprovalEntityRepository extends JpaRepository<ApprovalEntity,Integer> {

    @Query( value = "select * from APRV where emp_no=:empNo" , nativeQuery = true )
    List<ApprovalEntity> findByAllempNo(String empNo );


}
