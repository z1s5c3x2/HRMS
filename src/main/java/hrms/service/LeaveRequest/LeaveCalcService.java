package hrms.service.LeaveRequest;

import hrms.model.repository.LeaveRequestEntityRepository;
import hrms.model.repository.RetiredEmployeeEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Map;

@Service
public class LeaveCalcService {
    @Autowired
    LeaveRequestEntityRepository leaveRequestEntityRepository;

    @Transactional
    public int calcRestCount(String eno)
    {
        Map<Object,Integer> resultMap = leaveRequestEntityRepository.getRestCountByEmpNo(eno,String.valueOf(LocalDate.now().getYear()));
/*        System.out.println("objects.toString() = " + resultMap.toString());
        System.out.println("objects.get(\"ph\") = " + resultMap.get("ph"));
        System.out.println("objects.get(\"cnt\") = " + resultMap.get("cnt"));*/
        int myRest = 15; //15 고정 스타트
        int ph = Integer.parseInt(String.valueOf(resultMap.get("ph")));
        myRest += ph > 3 ? ((int)Math.floor( (ph-3) / 2 )+1) : 0;
        return myRest - Integer.parseInt(String.valueOf(resultMap.get("cnt"))) ;


    }
}
