package hrms.controller.attendance;

import hrms.model.dto.AttendanceDto;
import hrms.service.attendance.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/attendence")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    //출근처리
    @PostMapping("/")
    public boolean setAttendanceGoWork(@RequestBody AttendanceDto attendDto) {
        return attendanceService.setAttendanceGoWork(attendDto);
    }

    //근태출력 - 달력에 표시할 모든사원 or 사원하나
    @GetMapping("/getList")
    public boolean getAttendanceListAll(){
        return false;
    }


    //출결조회 - 출근지각/결근/ 모든사원 or 사원하나
    @GetMapping("/dailyAttd")
    public boolean getAttendanceInfo(){
        return false;
    }

    //근무시간조회 - 모든사원 or 한명의 사원
    @GetMapping("/workingTime")
    public boolean getWorkTime(){
        return false;
    }
    
    //퇴근처리
    @PutMapping("/")
    public boolean setAttendanceLeaveWork(@RequestBody AttendanceDto attendDto) {
        return attendanceService.setAttendanceLeaveWork(attendDto);
    }




}
