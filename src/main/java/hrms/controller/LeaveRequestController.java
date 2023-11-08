package hrms.controller;

import hrms.model.dto.LeaveRequestDto;
import hrms.model.dto.SalaryDto;
import hrms.service.LeaveRequest.LeaveRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/leaveRequest")
public class LeaveRequestController {
    @Autowired
    private LeaveRequestService leaveRequestService;

    @PostMapping("/post") // 연차 신청
    public boolean lrqWrite(@RequestBody LeaveRequestDto leaveRequestDto ){
        return leaveRequestService.lrqWrite( leaveRequestDto );
    }

    // 2. 모든 LRQ 리스트 출력
    @GetMapping("/getAll")
    public List<LeaveRequestDto> lrqGetAll(){

        return leaveRequestService.lrqGetAll();
    }
    // 개별 LRQ 출력
    @GetMapping("/get")
    public LeaveRequestDto lrqGet( int lrqNo ){

        return leaveRequestService.lrqGet( lrqNo );
    }
    @GetMapping("/getMe") // 메인페이지전용 세션에서 들어온 본인 사번으로 본인정보 출력
    public List<LeaveRequestDto> lrqGetMeAll(String empNo){

        return leaveRequestService.lrqGetMeAll(empNo);
    }
    // 3.
    @PutMapping("/put")
    public boolean lrqUpdate( LeaveRequestDto leaveRequestDto ){
        return leaveRequestService.lrqUpdate( leaveRequestDto );
    }
    // 4
    @DeleteMapping("/delete")
    public boolean lrqDelete( @RequestParam int lrqNo){
        return leaveRequestService.lrqDelete( lrqNo );
    }


}
