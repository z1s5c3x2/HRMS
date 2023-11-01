package hrms.controller;

import hrms.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/attendence")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;


    //근태출력 - 달력에 표시할 모든사원 or 사원하나
    
    //출결조회 - 출근지각/결근/ 모든사원 or 사원하나
    
    //근무시간조회 - 모든사원 or 한명의 사원

    //휴가 /병가 등록 하나의 사원


}
