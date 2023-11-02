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
        System.out.println("objects.toString() = " + resultMap.toString());
        System.out.println("objects.get(\"ph\") = " + resultMap.get("ph"));
        System.out.println("objects.get(\"cnt\") = " + resultMap.get("cnt"));
        int myRest = 15;
        if(Integer.parseInt(String.valueOf(resultMap.get("ph"))) > 2)
        {
            myRest += Math.round(Integer.parseInt(String.valueOf(resultMap.get("ph")))/2) - Integer.parseInt(String.valueOf(resultMap.get("cnt")));
            return myRest;
        }else {
            return myRest - Integer.parseInt(String.valueOf(resultMap.get("cnt"))) ;
        }


    }
}
