package hrms.controller.attendance;

import hrms.model.dto.AttendanceDto;
import hrms.service.attendance.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    //출근처리
    @PostMapping("/")
    public boolean setAttendanceGoWork(@RequestBody AttendanceDto attendDto) {
        return attendanceService.setAttendanceGoWork(attendDto);
    }
    //퇴근처리
    @PutMapping("/")
    public boolean setAttendanceLeaveWork( AttendanceDto attendDto) {
        return attendanceService.setAttendanceLeaveWork(attendDto);
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
    
    //전사원 근무 현황
    @GetMapping("/allempAttdList")
    public Page<AttendanceDto> allEmpAttdList(@RequestParam LocalDate periodStart,
                                              @RequestParam LocalDate periodEnd,
                                              @RequestParam int page,
                                              @RequestParam int dptmNo,
                                              @RequestParam int empRk)
    {
        System.out.println("start = " + periodStart + ", end = " + periodEnd + ", page = " + page + ", dptmNo = " + dptmNo + ", empRk = " + empRk);
        System.out.println(attendanceService.allEmpAttdList(periodStart,periodEnd,page,dptmNo,empRk));
        return null;
    }
}
