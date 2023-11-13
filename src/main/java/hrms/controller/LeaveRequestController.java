package hrms.controller;

import hrms.model.dto.ApprovalRequestDto;
import hrms.model.dto.LeaveRequestDto;
import hrms.model.dto.PageDto;
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
    public boolean lrqWrite(@RequestBody ApprovalRequestDto<LeaveRequestDto> approvalRequestDto ){
        return leaveRequestService.lrqWrite( approvalRequestDto );
    }

    // 2. 모든 LRQ 리스트 출력
    @GetMapping("/getAll")
    public PageDto lrqGetAll(@RequestParam int page  ,
                             @RequestParam String key ,
                             @RequestParam String keyword ,
                             @RequestParam int view ){

        return leaveRequestService.lrqGetAll(page , key , keyword , view);
    }

    @GetMapping("/get") // 메인페이지전용 세션에서 들어온 본인 사번으로 본인정보 출력
    public PageDto lrqGet(@RequestParam int page  , @RequestParam int view , @RequestParam String empNo ){

        return leaveRequestService.lrqGet(page , view , empNo);
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
