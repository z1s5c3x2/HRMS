package hrms.service.attendance;

import hrms.model.repository.AttendanceEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceEntityRepository attendanceRepository;


}
