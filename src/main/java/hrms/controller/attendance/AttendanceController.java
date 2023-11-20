package hrms.controller.attendance;

import hrms.model.dto.AttendanceDto;
import hrms.model.dto.PageDto;
import hrms.service.attendance.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    //출근처리
    @PostMapping("/")
    public boolean setAttendanceGoWork(@RequestBody AttendanceDto attendDto) {
        System.out.println("AttendanceController.setAttendanceGoWork");
        return attendanceService.setAttendanceGoWork(attendDto);
    }
    //퇴근처리
    @PutMapping("/")
    public boolean setAttendanceLeaveWork( AttendanceDto attendDto) {
        System.out.println("AttendanceController.setAttendanceLeaveWork");
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
    public PageDto<AttendanceDto> allEmpAttdList(@RequestParam String periodStart,
                                                 @RequestParam String periodEnd,
                                                 @RequestParam int page,
                                                 @RequestParam int dptmNo,
                                                 @RequestParam int empRk,
                                                 @RequestParam int keywordType,
                                                 @RequestParam String keyword)
    {
        System.out.println("periodStart = " + periodStart + ", periodEnd = " + periodEnd + ", page = " + page + ", dptmNo = " + dptmNo + ", empRk = " + empRk + ", keywordType = " + keywordType + ", keyword = " + keyword);
        return attendanceService.allEmpAttdList(periodStart,periodEnd,page,dptmNo,empRk,keywordType,keyword);
    }

    @GetMapping("/getMonthChart") // 나의 출결 현황
    public List<AttendanceDto> getMonthChart(@RequestParam int year, @RequestParam int month)
    {

        return attendanceService.getMonthChart( year, month);
    }
    @GetMapping("/checkWork")
    public boolean checkMyWork(){
        return attendanceService.checkMyWrok();
    }
    @GetMapping("/getmylrqChart")
    public List<AttendanceDto> getlrqChart(@RequestParam int year, @RequestParam int month){
        System.out.println("AttendanceController.getlrqChart");
        return attendanceService.getlrqChart( year, month);

    }
}