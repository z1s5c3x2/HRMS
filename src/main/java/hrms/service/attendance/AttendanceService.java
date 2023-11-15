package hrms.service.attendance;

import hrms.model.dto.AttendanceDto;
import hrms.model.entity.AttendanceEntity;
import hrms.model.repository.AttendanceEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceEntityRepository attendanceRepository;

    @Transactional
    public boolean setAttendanceGoWork(AttendanceDto attendDto) {
        AttendanceEntity attendanceEntity = attendanceRepository.save(attendDto.toEntity());
        if(attendanceEntity.getAttdNo() > 0) {return true;}
        return false;
    }
    @Transactional
    public boolean setAttendanceLeaveWork(@RequestBody AttendanceDto attendDto) {
        // pk(empNo)와  오늘날짜의 출근정보 검색

        // Optional<AttendanceEntity> attendEntity = AttendanceEntityRepository.findById(attendDto.getEmpNo());
        //결과가 있으면 퇴근정보 update
        
        
        //결과 없으면  false반환
        
        // 해당 attpNo에
        return false;
        
    }



}
